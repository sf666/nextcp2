package nextcp.ai.mcp;

import java.util.Collection;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaServerDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.DeviceRegistry;

/**
 * Defines MCP services related to UPnP device management, such as listing
 * available Media Renderers and selecting a specific renderer for interaction.
 *
 * These services are intended to be called by the LLM when it needs to manage
 * or interact with UPnP devices in the system.
 * 
 */
@Service
public class McpDeviceServer {

	private static final Logger log = LoggerFactory.getLogger(McpDeviceServer.class);

	@Autowired
	private DeviceRegistry deviceRegistry = null;

	@Autowired
	private DtoBuilder dtoBuilder;

	@Autowired
	private MessageSource messageSource;

	private MediaRendererDto selectedMediaRenderer = null;

	private MediaServerDto selectedMediaServer = null;

	// Renderer
	//
	@Tool(name = "list_renderers", description = """
		Returns a list of available Media Renderer UPnP devices (also called players) which are online.

		Do not use this tool for getting the active or currently selected Media Renderer.
			""")
	public Collection<MediaRendererDto> listMediaRenderer() {
		log.info("UPnP command received: List Media Renderer");

		Collection<MediaRendererDto> devices = dtoBuilder.getMediaRendererAsDto(deviceRegistry.getAvailableMediaRenderer());

		return devices;
	}

	@Tool(name = "selected_renderer",     description = """
        Returns the currently selected Media Renderer (UPnP device, also called player).
        
        Use this tool when:
        - The user asks about the active playback device.
        - You need to know which speaker/player is currently controlled.
        """)
	public String getSelectedMediaRenderer() {
		log.info("UPnP command received: Get selected Media Renderer");
		if (this.selectedMediaServer == null) {
			return messageSource.getMessage("mcp.device.renderer.select.noSelection.response", null, Locale.getDefault());
		}
		
		var found = deviceRegistry.getAvailableMediaRenderer().stream().filter(renderer -> renderer.getUdnAsString().equals(selectedMediaRenderer.udn))
			.findFirst();

		if (found.isPresent()) {
			return messageSource.getMessage("mcp.device.renderer.select.response", new Object[] { selectedMediaRenderer.friendlyName }, Locale.getDefault());
		} else {
			return messageSource.getMessage("mcp.device.renderer.select.notFound.response", new Object[] { selectedMediaRenderer.friendlyName },
				Locale.getDefault());
		}
	}

	@Tool(name = "select_renderer", description = "Selects a Media Renderer for interaction with the LLM.")
	public String selectMediaRenderer(
		@ToolParam(description = "The exact UDN of the Media Renderer to control", required = true) String udn) {
		log.info("UPnP command received: Select Media Renderer - {}", udn);
		var found = deviceRegistry.getAvailableMediaRenderer().stream().filter(renderer -> renderer.getUdnAsString().equals(udn))
			.findFirst();

		if (found.isPresent()) {
			selectedMediaRenderer = found.get().getAsDto();
			return messageSource.getMessage("mcp.device.renderer.select.response", new Object[] { selectedMediaRenderer.friendlyName }, Locale.getDefault());
		} else {
			selectedMediaRenderer = null;
			return messageSource.getMessage("mcp.device.renderer.select.notFound.response", new Object[] { selectedMediaRenderer.friendlyName },
				Locale.getDefault());
		}
	}

	// Server
	//
	@Tool(name = "list_server", description = """
		Returns a list of available Media Server UPnP devices which are online.
		
		Do not use this tool for getting the active or currently selected Media Server.
		""")
	public Collection<MediaServerDto> listMediaServer() {
		log.info("UPnP command received: Liste Media Server");

		Collection<MediaServerDto> devices = dtoBuilder.getMediaServerAsDto(deviceRegistry.getAvailableMediaServer());

		return devices;
	}

	@Tool(name = "selected_server", description = """
        Returns the currently selected Media Server (UPnP device).
        Use this tool when:
        - The user wants to know on which server music, videos, or photos come from.
        - You need to know the source/library where media files are stream from.
        - The user asks 'Where is my music coming from?' or 'Which media server is active? '.
        """)
	public String getSelectedMediaServer() {
		log.info("UPnP command received: Get selected Media Server");

		if(this.selectedMediaServer == null) {
			return messageSource.getMessage("mcp.device.server.select.noSelection.response", null, Locale.getDefault());
		}
		
		var found = deviceRegistry.getAvailableMediaServer().stream().filter(server -> server.getUdnAsString().equals(selectedMediaServer.udn))
			.findFirst();

		if (found.isPresent()) {
			return messageSource.getMessage("mcp.device.server.select.response", new Object[] { selectedMediaServer.friendlyName }, Locale.getDefault());
		} else {
			return messageSource.getMessage("mcp.device.server.select.notFound.response", new Object[] { selectedMediaServer.friendlyName },
				Locale.getDefault());
		}
	}
	
	@Tool(name = "select_server", description = "Selects a Media Server for interaction with the LLM.")
	public String selectMediaServer(
		@ToolParam(description = "The exact UDN of the Media Server to control", required = true) String udn) {
		log.info("UPnP command received: Select Media Server - {}", udn);
		var found = deviceRegistry.getAvailableMediaServer().stream().filter(server -> server.getUdnAsString().equals(udn))
			.findFirst();

		if (found.isPresent()) {
			selectedMediaServer = found.get().getAsDto();
			return messageSource.getMessage("mcp.device.server.select.response", new Object[] { selectedMediaServer.friendlyName }, Locale.getDefault());
		} else {
			selectedMediaServer = null;
			return messageSource.getMessage("mcp.device.server.select.notFound.response", new Object[] { selectedMediaServer.friendlyName },
				Locale.getDefault());
		}
	}

}