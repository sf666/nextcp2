package nextcp.ai.mcp;

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
 * Defines MCP services related to UPnP device management, such as listing
 * available Media Renderers and selecting a specific renderer for interaction.
 *
 * These services are intended to be called by the LLM when it needs to manage
 * or interact with UPnP devices in the system.
 */
@Service
public class McpDeviceServer {

	private static final Logger log = LoggerFactory.getLogger(McpDeviceServer.class);

	@Autowired
	private MessageSource messageSource;

	@Tool(name = "list_renderers", description = "Liefert eine Liste der verfügbaren Media Renderer UPnP-Geräte (auch Player genannt).")
	public String listMediaRenderer() {
		log.info("UPnP command received: Liste Media Renderer");

		List<String> devices = List.of("Wohnzimmer Lautsprecher", "Küche Radio", "Schlafzimmer Soundbar");

		StringBuilder sb = new StringBuilder();
		sb.append(messageSource.getMessage("mcp.device.list.header", null, Locale.getDefault()));
		sb.append("\n");
		for (int i = 0; i < devices.size(); i++) {
			sb.append(devices.get(i));
			sb.append("\n");
		}
		return sb.toString();
	}

	@Tool(name = "select_renderer", description = "Wählt einen Media Renderer aus, für die interaktion mit dem LLM.")
	public String selectMediaRenderer(
		@ToolParam(description = "Der exakte Name des zu steuernden Media Renderers", required = true) String mediaRendererName) {
		log.info("UPnP command received: Select Media Renderer - {}", mediaRendererName);
		return messageSource.getMessage("mcp.device.renderer.select.response", new Object[] { mediaRendererName }, Locale.getDefault());
	}
}