package nextcp.rest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextcp.ai.AiServices;
import nextcp.ai.BudgetExceededException;
import nextcp.ai.ChatHistoryService;
import nextcp.ai.ModelUnavailableException;
import nextcp.ai.mcp.McpLocale;
import nextcp.dto.ChatHistoryDto;
import nextcp.dto.SelectedDevicesDto;

@RestController
@RequestMapping("/api/ai")
public class RestAiServices {

	private static final Logger log = LoggerFactory.getLogger(RestAiServices.class.getName());

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private AiServices aiServices;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private McpLocale mcpLocale;

	@Autowired
	private ChatHistoryService chatHistoryService;

	public RestAiServices() {
	}

	/**
	 * Returns the currently selected Media Renderer and Media Server as managed
	 * by the LLM via the MCP tools. Either field of the response may be
	 * {@code null} when no device of that type has been selected yet.
	 */
	@GetMapping(value = "/selectedDevices", produces = MediaType.APPLICATION_JSON_VALUE)
	public SelectedDevicesDto getSelectedDevices() {
		return aiServices.getSelectedDevices();
	}

	/**
	 * Returns the in-memory chat history so a browser reload can restore the
	 * previous conversation. Pending entries indicate that a response is still
	 * being computed by the AI provider. Clients no longer need to poll this
	 * endpoint: subsequent state changes are pushed via the SSE event
	 * {@code CHAT_HISTORY_CHANGED}. This endpoint is only used for the initial
	 * restore on page load.
	 */
	@GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ChatHistoryDto getHistory() {
		return chatHistoryService.getHistory();
	}

	@PostMapping(value = "/doAction", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter sendTextToGemini(@RequestBody String userText) {
		SseEmitter emitter = new SseEmitter(120000L);

		// Record the exchange in the history BEFORE kicking off the async task.
		// startExchange pushes a CHAT_HISTORY_CHANGED SSE event so connected
		// clients immediately see the PENDING placeholder; the eventual answer
		// is pushed the same way when completeExchange / failExchange runs.
		String extractedMessage = extractMessage(userText);
		long assistantId = chatHistoryService.startExchange(extractedMessage);

		CompletableFuture.runAsync(() -> {
			try {
				String response = aiServices.sendTextToGemini(extractedMessage);
				chatHistoryService.completeExchange(assistantId, response);
				sendSuccessResponse(emitter, response);
			} catch (BudgetExceededException e) {
				// AI provider quota / budget exhausted - report a friendly,
				// localized message to the caller instead of leaking the stack trace.
				log.warn("AI request rejected: budget/quota exceeded. {}", e.getMessage());
				String friendly = messageSource.getMessage("mcp.ai.budgetExceeded.response", null, mcpLocale.getCurrentLocale());
				chatHistoryService.failExchange(assistantId, friendly);
				sendErrorResponse(emitter, friendly, e);
			} catch (ModelUnavailableException e) {
				// AI model temporarily overloaded / unavailable (e.g. HTTP 503).
				// Report a friendly, retryable message instead of leaking the stack trace.
				log.warn("AI request failed: model temporarily unavailable. {}", e.getMessage());
				String friendly = messageSource.getMessage("mcp.ai.modelUnavailable.response", null, mcpLocale.getCurrentLocale());
				chatHistoryService.failExchange(assistantId, friendly);
				sendErrorResponse(emitter, friendly, e);
			} catch (Exception e) {
				log.error("Error processing AI request:", e);
				String msg = "Error processing AI request: " + e.getMessage();
				chatHistoryService.failExchange(assistantId, msg);
				sendErrorResponse(emitter, msg, e);
			}
		});

		return emitter;
	}

	/**
	 * Sends the AI response as a JSON payload over the SSE emitter and
	 * completes it. Mirrors {@link #sendErrorResponse(SseEmitter, String, Exception)}
	 * so both branches use the same wire format.
	 */
	private void sendSuccessResponse(SseEmitter emitter, String response) {
		try {
			emitter.send(SseEmitter.event().data(Map.of("response", response), MediaType.APPLICATION_JSON));
			emitter.send(SseEmitter.event().data("[DONE]", MediaType.TEXT_PLAIN));
			emitter.complete();
		} catch (IOException ioException) {
			// Client disconnected (e.g. browser reload). The history already
			// holds the response and a CHAT_HISTORY_CHANGED event was pushed,
			// so reconnecting clients receive it via SSE (or the initial
			// /history fetch on reload).
			log.info("SSE response could not be delivered (client likely disconnected); response is preserved in history: {}",
				ioException.getMessage());
			emitter.completeWithError(ioException);
		}
	}

	/**
	 * Sends a single response payload over the SSE emitter and completes it.
	 * Used by the error branches so the client always receives a JSON response
	 * followed by the [DONE] marker, matching the success path.
	 */
	private void sendErrorResponse(SseEmitter emitter, String userFacingMessage, Exception originalError) {
		try {
			emitter.send(SseEmitter.event().data(Map.of("response", userFacingMessage), MediaType.APPLICATION_JSON));
			emitter.send(SseEmitter.event().data("[DONE]", MediaType.TEXT_PLAIN));
			emitter.complete();
		} catch (IOException ioException) {
			log.warn("Could not send error response over SSE: {}", ioException.getMessage());
			emitter.completeWithError(originalError);
		}
	}

	private String extractMessage(String input) {
		if (input == null || input.trim().isEmpty()) {
			return "";
		}
		try {
			if (input.trim().startsWith("{")) {
				JsonNode rootNode = objectMapper.readTree(input);
				if (rootNode.has("message")) {
					return rootNode.get("message").asText();
				}
			}
		} catch (Exception e) {
			log.warn("Input looked like JSON but could not be parsed, using raw input. Error: {}", e.getMessage());
		}
		return input;
	}
}
