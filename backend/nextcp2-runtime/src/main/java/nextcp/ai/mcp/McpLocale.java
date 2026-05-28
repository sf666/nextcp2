package nextcp.ai.mcp;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Defines MCP services related to language/locale management. The LLM can use
 * these tools to list the supported languages, query the currently selected
 * language, and switch the active language.
 *
 * Supported languages:
 * <ul>
 *   <li>EN - English (default)</li>
 *   <li>DE - German</li>
 * </ul>
 */
@Service
public class McpLocale {

	private static final Logger log = LoggerFactory.getLogger(McpLocale.class);

	/**
	 * Supported language codes (ISO 639-1, upper case).
	 */
	private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("EN", "DE");

	/**
	 * Default language used when no explicit selection was made.
	 */
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Currently selected locale. Defaults to English.
	 */
	private Locale selectedLocale = DEFAULT_LOCALE;

	@Tool(name = "list_languages", description = """
		Returns the list of languages supported by this control point.

		Use this tool when the user asks which languages are available or how
		they can switch the system language.
		""")
	public List<String> listLanguages() {
		log.info("Locale command received: List supported languages");
		return SUPPORTED_LANGUAGES;
	}

	@Tool(name = "selected_language", description = """
		Returns the currently selected language of the control point.

		Use this tool when:
		- The user asks which language is currently active.
		- You need to know in which language to respond.
		""")
	public String getSelectedLanguage() {
		log.info("Locale command received: Get selected language");
		if (selectedLocale == null) {
			return messageSource.getMessage("mcp.locale.selected.noSelection.response", null, DEFAULT_LOCALE);
		}
		String languageCode = selectedLocale.getLanguage().toUpperCase(Locale.ROOT);
		return messageSource.getMessage("mcp.locale.selected.response", new Object[] { languageCode }, selectedLocale);
	}

	@Tool(name = "select_language", description = """
		Selects the active language of the control point. All further responses
		will be in the selected language.

		Supported language codes are: EN (English, default) and DE (German).
		""")
	public String selectLanguage(
		@ToolParam(description = "ISO 639-1 language code to activate. Supported values: EN, DE.", required = true) String languageCode) {
		log.info("Locale command received: Select language - {}", languageCode);

		if (languageCode == null) {
			return messageSource.getMessage("mcp.locale.select.notFound.response", new Object[] { "" }, selectedLocale);
		}

		String normalized = languageCode.trim().toUpperCase(Locale.ROOT);

		if (!SUPPORTED_LANGUAGES.contains(normalized)) {
			return messageSource.getMessage("mcp.locale.select.notFound.response", new Object[] { languageCode }, selectedLocale);
		}

		selectedLocale = Locale.forLanguageTag(normalized.toLowerCase(Locale.ROOT));
		// Set the JVM default locale so that other MCP tools using Locale.getDefault()
		// also produce responses in the newly selected language.
		Locale.setDefault(selectedLocale);

		return messageSource.getMessage("mcp.locale.select.response", new Object[] { normalized }, selectedLocale);
	}

	/**
	 * Returns the currently active {@link Locale}. Other components can use this
	 * to render localized responses consistently with the LLM-driven selection.
	 *
	 * @return the currently selected locale, never {@code null}
	 */
	public Locale getCurrentLocale() {
		return selectedLocale != null ? selectedLocale : DEFAULT_LOCALE;
	}
}
