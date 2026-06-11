package nextcp.ai;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import nextcp.ai.mcp.McpLocale;
import nextcp.dto.AiConfig;

/**
 * Posts a chat notice whenever the active AI provider or model changed, so the
 * user sees in the chat that a switch happened. The message is localized using
 * the same {@link MessageSource} / {@link McpLocale} mechanism as the other
 * AI-facing responses.
 */
@Component
public class AiProviderChangeNotifier {

	private static final Logger log = LoggerFactory.getLogger(AiProviderChangeNotifier.class);

	private final ChatHistoryService chatHistoryService;
	private final MessageSource messageSource;
	private final McpLocale mcpLocale;

	public AiProviderChangeNotifier(ChatHistoryService chatHistoryService, MessageSource messageSource, McpLocale mcpLocale) {
		this.chatHistoryService = chatHistoryService;
		this.messageSource = messageSource;
		this.mcpLocale = mcpLocale;
	}

	/**
	 * Emits a chat notice when the provider or model differs from the previous
	 * values. No notice is posted when nothing relevant changed or when the new
	 * provider is blank (AI effectively turned off).
	 *
	 * @param previousProvider provider before the change (may be {@code null})
	 * @param previousModel    model before the change (may be {@code null})
	 * @param current          the AI configuration after the change
	 */
	public void notifyProviderChanged(String previousProvider, String previousModel, AiConfig current) {
		if (current == null || StringUtils.isBlank(current.aiProvider)) {
			return;
		}
		boolean changed = !Objects.equals(previousProvider, current.aiProvider)
			|| !Objects.equals(previousModel, current.aiModel);
		if (!changed) {
			return;
		}

		String notice = messageSource.getMessage("mcp.ai.providerChanged.notice",
			new Object[] { current.aiProvider, current.aiModel }, mcpLocale.getCurrentLocale());
		log.info("AI provider/model changed ({} / {} -> {} / {}); posting chat notice.", previousProvider, previousModel,
			current.aiProvider, current.aiModel);
		chatHistoryService.addAssistantNotice(notice);
	}
}
