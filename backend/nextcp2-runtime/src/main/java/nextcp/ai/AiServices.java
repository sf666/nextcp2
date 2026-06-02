package nextcp.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.ai.mcp.McpDevices;
import nextcp.dto.SelectedDevicesDto;

/**
 * This service
 */
@Service
public class AiServices {
	private static final Logger log = LoggerFactory.getLogger(AiServices.class.getName());

	private final ChatClient chatClient;

	@Autowired
	private McpDevices mcpDevices;

	public AiServices(@Autowired(required = false) ChatClient chatClient) {
		this.chatClient = chatClient;
	}


	public String sendTextToGemini(String userText) {
		if (this.chatClient == null) {
			throw new RuntimeException("ChatClient  not initialized! Please ensure it is properly configured.");
		}
		log.info("Sending text to Gemini: {}", userText);

		try {
			// send text and integrated @McpTools to GEMENI
			String retVal = chatClient.prompt().user(userText).call().content();

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
