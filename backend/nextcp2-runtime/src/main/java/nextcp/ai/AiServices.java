package nextcp.ai;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.ai.mcp.McpDevices;
import nextcp.config.ChatClientProvider;
import nextcp.dto.ChatHistoryDto;
import nextcp.dto.ChatMessageDto;
import nextcp.dto.Config;
import nextcp.dto.SelectedDevicesDto;

/**
 * This service
 */
@Service
public class AiServices {
	private static final Logger log = LoggerFactory.getLogger(AiServices.class.getName());

	/** Upper bound of prior messages replayed as conversation memory (token control). */
	private static final int MEMORY_MAX_MESSAGES = 20;

	private final ChatClientProvider chatClientProvider;

	@Autowired
	private McpDevices mcpDevices;

	@Autowired
	private ChatHistoryService chatHistoryService;

	@Autowired
	private Config config;

	public AiServices(ChatClientProvider chatClientProvider) {
		this.chatClientProvider = chatClientProvider;
	}


	public String sendTextToGemini(String userText) {
		// Resolve the ChatClient for the currently configured provider. It is rebuilt
		// automatically when the AI configuration changed, so no restart is needed.
		ChatClient chatClient = chatClientProvider.getChatClient();
		if (chatClient == null) {
			throw new RuntimeException("ChatClient not initialized! Please check the AI configuration (provider/model/baseUrl/apiKey).");
		}
		log.info("Sending text to Gemini: {}", userText);

		try {
			String retVal;
			if (isConversationMemoryEnabled()) {
				// Replay the prior conversation (incl. the current user message, which the
				// caller already added to the history) so the model has context.
				List<Message> memory = buildMemoryMessages();
				if (memory.isEmpty()) {
					retVal = chatClient.prompt().user(userText).call().content();
				} else {
					retVal = chatClient.prompt().messages(memory).call().content();
				}
			} else {
				// Stateless: send only the current user message (and the @McpTools).
				retVal = chatClient.prompt().user(userText).call().content();
			}

			log.info("Received response from Gemini: {}", retVal);
			return retVal;
		} catch (RuntimeException ex) {
			if (isQuotaExceeded(ex)) {
				log.warn("AI provider rejected request because the quota/budget is exceeded: {}", rootMessage(ex));
				throw new BudgetExceededException("AI quota exceeded", ex);
			}
			if (isModelUnavailable(ex)) {
				log.warn("AI provider temporarily unavailable (model overloaded): {}", rootMessage(ex));
				throw new ModelUnavailableException("AI model temporarily unavailable", ex);
			}
			throw ex;
		}
	}

	private boolean isConversationMemoryEnabled() {
		return config != null && config.aiConfig != null && Boolean.TRUE.equals(config.aiConfig.aiConversationMemory);
	}

	/**
	 * Builds the conversation-memory message list from the current chat history.
	 * Only completed USER/ASSISTANT turns are included (the pending assistant
	 * placeholder and ERROR entries are skipped); the window is capped at
	 * {@link #MEMORY_MAX_MESSAGES} to limit token usage. The current user message
	 * is the last completed entry, so no separate {@code .user(...)} call is needed.
	 */
	private List<Message> buildMemoryMessages() {
		List<Message> result = new ArrayList<>();
		ChatHistoryDto history = chatHistoryService.getHistory();
		if (history == null || history.messages == null) {
			return result;
		}

		List<ChatMessageDto> completed = new ArrayList<>();
		for (ChatMessageDto m : history.messages) {
			boolean isUserOrAssistant = ChatHistoryService.ROLE_USER.equals(m.role) || ChatHistoryService.ROLE_ASSISTANT.equals(m.role);
			if (ChatHistoryService.STATUS_COMPLETE.equals(m.status) && isUserOrAssistant) {
				completed.add(m);
			}
		}

		int from = Math.max(0, completed.size() - MEMORY_MAX_MESSAGES);
		for (int i = from; i < completed.size(); i++) {
			ChatMessageDto m = completed.get(i);
			String content = m.content == null ? "" : m.content;
			if (ChatHistoryService.ROLE_USER.equals(m.role)) {
				result.add(new UserMessage(content));
			} else {
				result.add(new AssistantMessage(content));
			}
		}
		return result;
	}

	/**
	 * Checks whether the given exception (or any of its causes) indicates that
	 * the AI provider's quota / budget has been exceeded. Google Gemini reports
	 * this as a {@code com.google.genai.errors.ClientException} carrying HTTP
	 * status 429. The class is matched by name to avoid a hard compile-time
	 * dependency on the provider SDK.
	 */
	private boolean isQuotaExceeded(Throwable throwable) {
		Throwable current = throwable;
		while (current != null) {
			String className = current.getClass().getName();
			String message = current.getMessage();
			boolean isClientException = "com.google.genai.errors.ClientException".equals(className);
			boolean messageIndicatesQuota = message != null
				&& (message.contains("429") || message.toLowerCase().contains("exceeded your current quota")
					|| message.toLowerCase().contains("quota exceeded"));
			if (isClientException && messageIndicatesQuota) {
				return true;
			}
			if (messageIndicatesQuota && message.contains("429")) {
				return true;
			}
			current = current.getCause();
		}
		return false;
	}

	/**
	 * Checks whether the given exception (or any of its causes) indicates that
	 * the AI provider is temporarily unavailable / overloaded. Google Gemini
	 * reports this as a {@code com.google.genai.errors.ServerException} carrying
	 * HTTP status 503 ("This model is currently experiencing high demand").
	 * Matched by class name / message to avoid a hard compile-time dependency on
	 * the provider SDK.
	 */
	private boolean isModelUnavailable(Throwable throwable) {
		Throwable current = throwable;
		while (current != null) {
			String className = current.getClass().getName();
			String message = current.getMessage();
			String lower = message == null ? "" : message.toLowerCase();
			boolean isServerException = "com.google.genai.errors.ServerException".equals(className);
			boolean messageIndicatesUnavailable = message != null
				&& (message.contains("503") || lower.contains("high demand") || lower.contains("overloaded")
					|| lower.contains("try again later") || lower.contains("unavailable"));
			if ((isServerException && messageIndicatesUnavailable) || (message != null && message.contains("503"))) {
				return true;
			}
			current = current.getCause();
		}
		return false;
	}

	private String rootMessage(Throwable throwable) {
		Throwable current = throwable;
		while (current.getCause() != null) {
			current = current.getCause();
		}
		return current.getMessage();
	}

	/**
	 * Returns the currently selected Media Renderer and Media Server as a single
	 * DTO. Either field may be {@code null} when no device of the given type has
	 * been selected yet by the LLM.
	 *
	 * @return DTO containing the currently selected devices; never {@code null}
	 */
	public SelectedDevicesDto getSelectedDevices() {
		SelectedDevicesDto dto = new SelectedDevicesDto();
		dto.mediaRenderer = mcpDevices.getSelectedMediaRendererDto();
		dto.mediaServer = mcpDevices.getSelectedMediaServerDto();
		return dto;
	}
}
