package nextcp.ai;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nextcp.dto.ChatHistoryDto;
import nextcp.dto.ChatMessageDto;
import nextcp.dto.Config;

/**
 * In-memory chat history for the AI chat. Holds a bounded ring buffer of
 * {@link ChatMessageDto} entries, so that a browser reload can restore the
 * previous conversation including any answer that arrived while the browser
 * was disconnected.
 *
 * <p>The maximum number of retained entries is read from
 * {@code applicationConfig.chatHistorySize} (default {@link #DEFAULT_HISTORY_SIZE}).
 *
 * <p>This service is process-local and lives only for the lifetime of the
 * Spring Boot process; it does not survive a backend restart. All public
 * methods are thread-safe.
 */
@Service
public class ChatHistoryService {

	private static final Logger log = LoggerFactory.getLogger(ChatHistoryService.class);

	public static final int DEFAULT_HISTORY_SIZE = 50;

	public static final String ROLE_USER = "USER";
	public static final String ROLE_ASSISTANT = "ASSISTANT";

	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_COMPLETE = "COMPLETE";
	public static final String STATUS_ERROR = "ERROR";

	@Autowired
	private Config config;

	private final Deque<ChatMessageDto> messages = new ArrayDeque<>();

	private final AtomicLong idSequence = new AtomicLong(0L);

	/**
	 * Returns the configured maximum number of messages to keep in history.
	 * Falls back to {@link #DEFAULT_HISTORY_SIZE} when no positive value is
	 * configured.
	 */
	public int getMaxSize() {
		if (config != null && config.applicationConfig != null && config.applicationConfig.chatHistorySize != null
			&& config.applicationConfig.chatHistorySize > 0) {
			return config.applicationConfig.chatHistorySize;
		}
		return DEFAULT_HISTORY_SIZE;
	}

	/**
	 * Appends a completed user message and a pending assistant placeholder. The
	 * returned ID is the placeholder's ID and must be used to update the
	 * assistant message once the LLM has answered or an error occurred.
	 *
	 * @param userText the text the user sent
	 * @return ID of the pending assistant message that callers must complete
	 */
	public synchronized long startExchange(String userText) {
		long now = System.currentTimeMillis();

		ChatMessageDto userMessage = new ChatMessageDto();
		userMessage.id = idSequence.incrementAndGet();
		userMessage.role = ROLE_USER;
		userMessage.content = userText == null ? "" : userText;
		userMessage.status = STATUS_COMPLETE;
		userMessage.timestamp = now;
		append(userMessage);

		ChatMessageDto assistantPlaceholder = new ChatMessageDto();
		assistantPlaceholder.id = idSequence.incrementAndGet();
		assistantPlaceholder.role = ROLE_ASSISTANT;
		assistantPlaceholder.content = "";
		assistantPlaceholder.status = STATUS_PENDING;
		assistantPlaceholder.timestamp = now;
		append(assistantPlaceholder);

		log.debug("Started exchange: userId={}, pendingAssistantId={}", userMessage.id, assistantPlaceholder.id);
		return assistantPlaceholder.id;
	}

	/**
	 * Marks the assistant message with the given ID as completed and stores the
	 * response content. Silently ignored when the message has already been
	 * evicted (because the ring buffer overflowed).
	 */
	public synchronized void completeExchange(long assistantId, String content) {
		ChatMessageDto msg = findById(assistantId);
		if (msg == null) {
			log.warn("Cannot complete exchange - message id {} no longer in history", assistantId);
			return;
		}
		msg.content = content == null ? "" : content;
		msg.status = STATUS_COMPLETE;
		msg.timestamp = System.currentTimeMillis();
	}

	/**
	 * Marks the assistant message with the given ID as failed and stores a
	 * (typically user-facing) error description as its content.
	 */
	public synchronized void failExchange(long assistantId, String errorContent) {
		ChatMessageDto msg = findById(assistantId);
		if (msg == null) {
			log.warn("Cannot fail exchange - message id {} no longer in history", assistantId);
			return;
		}
		msg.content = errorContent == null ? "" : errorContent;
		msg.status = STATUS_ERROR;
		msg.timestamp = System.currentTimeMillis();
	}

	/**
	 * Returns a snapshot copy of the current history along with the configured
	 * maximum size, suitable for serialization to a REST client.
	 */
	public synchronized ChatHistoryDto getHistory() {
		ChatHistoryDto dto = new ChatHistoryDto();
		dto.messages = new ArrayList<>(messages);
		dto.max = getMaxSize();
		return dto;
	}

	private void append(ChatMessageDto msg) {
		messages.addLast(msg);
		trim();
	}

	private void trim() {
		int max = getMaxSize();
		while (messages.size() > max) {
			messages.removeFirst();
		}
	}

	private ChatMessageDto findById(long id) {
		for (ChatMessageDto m : messages) {
			if (m.id != null && m.id == id) {
				return m;
			}
		}
		return null;
	}
}
