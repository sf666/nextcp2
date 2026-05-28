package nextcp.ai.mcp;

import java.util.Collection;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import nextcp.config.FileConfigPersistence;
import nextcp.dto.AiConfig;
import nextcp.dto.Config;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaServerDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

/**
 * Defines MCP services related to UPnP device management, such as listing
 * available Media Renderers and selecting a specific renderer for interaction.
 *
 * These services are intended to be called by the LLM when it needs to manage
 * or interact with UPnP devices in the system.
 *
 * <p>The selection is persisted to {@link AiConfig#selectedRendererUdn} /
 * {@link AiConfig#selectedServerUdn} so it survives a backend restart. On
 * startup the persisted UDNs are resolved against the {@link DeviceRegistry};
 * if the device is not yet discovered the selection stays unresolved and is
 * re-resolved lazily on the next access.
 */
@Service
public class McpDevices {

	private static final Logger log = LoggerFactory.getLogger(McpDevices.class);

	@Autowired
	private DeviceRegistry deviceRegistry = null;

	@Autowired
	private DtoBuilder dtoBuilder;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private McpLocale mcpLocale;

	@Autowired
	private Config config;

	@Autowired
	private FileConfigPersistence configPersistence;

	private MediaRendererDto selectedMediaRenderer = null;

	private MediaServerDto selectedMediaServer = null;

	/**
	 * After bean initialization, try to resolve the persisted UDNs against the
	 * UPnP {@link DeviceRegistry}. UPnP discovery may not have completed yet at
	 * this point - in that case the DTOs remain null and are populated lazily
	 * via {@link #getSelectedMediaRendererDto()} / {@link #getSelectedMediaServerDto()}.
	 */
	@PostConstruct
	private void restoreSelectionFromConfig() {
		AiConfig ai = getAiConfig();
		if (ai == null) {
			return;
		}
		if (ai.selectedRendererUdn != null && !ai.selectedRendererUdn.isBlank()) {
			MediaRendererDto dto = resolveRendererByUdn(ai.selectedRendererUdn);
			if (dto != null) {
				selectedMediaRenderer = dto;
				log.info("Restored selected Media Renderer from config: udn={}, friendlyName={}", dto.udn, dto.friendlyName);
			} else {
				log.info("Persisted Media Renderer UDN {} not yet available in registry; will resolve lazily", ai.selectedRendererUdn);
			}
		}
		if (ai.selectedServerUdn != null && !ai.selectedServerUdn.isBlank()) {
			MediaServerDto dto = resolveServerByUdn(ai.selectedServerUdn);
			if (dto != null) {
				selectedMediaServer = dto;
				log.info("Restored selected Media Server from config: udn={}, friendlyName={}", dto.udn, dto.friendlyName);
			} else {
				log.info("Persisted Media Server UDN {} not yet available in registry; will resolve lazily", ai.selectedServerUdn);
			}
		}
	}

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
		// Refresh from registry in case the device was persisted but only just discovered.
		MediaRendererDto current = getSelectedMediaRendererDto();
		if (current == null) {
			return messageSource.getMessage("mcp.device.renderer.select.noSelection.response", null, mcpLocale.getCurrentLocale());
		}

		var found = deviceRegistry.getAvailableMediaRenderer().stream().filter(renderer -> renderer.getUdnAsString().equals(current.udn))
			.findFirst();

		if (found.isPresent()) {
			return messageSource.getMessage("mcp.device.renderer.select.response", new Object[] { current.friendlyName }, mcpLocale.getCurrentLocale());
		} else {
			return messageSource.getMessage("mcp.device.renderer.select.notFound.response", new Object[] { current.friendlyName },
				mcpLocale.getCurrentLocale());
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
			persistRendererUdn(selectedMediaRenderer.udn);
			return messageSource.getMessage("mcp.device.renderer.select.response", new Object[] { selectedMediaRenderer.friendlyName }, mcpLocale.getCurrentLocale());
		} else {
			// Unknown UDN; produce a friendly response using the requested UDN so
			// the LLM can see what it tried to select. Existing selection stays
			// untouched - we don't clear a valid selection on a typo.
			return messageSource.getMessage("mcp.device.renderer.select.notFound.response", new Object[] { udn },
				mcpLocale.getCurrentLocale());
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

		MediaServerDto current = getSelectedMediaServerDto();
		if (current == null) {
			return messageSource.getMessage("mcp.device.server.select.noSelection.response", null, mcpLocale.getCurrentLocale());
		}

		var found = deviceRegistry.getAvailableMediaServer().stream().filter(server -> server.getUdnAsString().equals(current.udn))
			.findFirst();

		if (found.isPresent()) {
			return messageSource.getMessage("mcp.device.server.select.response", new Object[] { current.friendlyName }, mcpLocale.getCurrentLocale());
		} else {
			return messageSource.getMessage("mcp.device.server.select.notFound.response", new Object[] { current.friendlyName },
				mcpLocale.getCurrentLocale());
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
			persistServerUdn(selectedMediaServer.udn);
			return messageSource.getMessage("mcp.device.server.select.response", new Object[] { selectedMediaServer.friendlyName }, mcpLocale.getCurrentLocale());
		} else {
			// Unknown UDN; keep existing selection intact.
			return messageSource.getMessage("mcp.device.server.select.notFound.response", new Object[] { udn },
				mcpLocale.getCurrentLocale());
		}
	}

	/**
	 * Returns the currently selected Media Renderer as DTO. Used by REST endpoints
	 * to expose the LLM-driven selection to the frontend. Will lazily resolve a
	 * persisted UDN whose device was not yet discovered at startup time.
	 *
	 * @return the selected {@link MediaRendererDto} or {@code null} if none is selected
	 */
	public MediaRendererDto getSelectedMediaRendererDto() {
		if (selectedMediaRenderer == null) {
			AiConfig ai = getAiConfig();
			if (ai != null && ai.selectedRendererUdn != null && !ai.selectedRendererUdn.isBlank()) {
				selectedMediaRenderer = resolveRendererByUdn(ai.selectedRendererUdn);
			}
		}
		return selectedMediaRenderer;
	}

	/**
	 * Returns the currently selected Media Server as DTO. Used by REST endpoints
	 * to expose the LLM-driven selection to the frontend. Will lazily resolve a
	 * persisted UDN whose device was not yet discovered at startup time.
	 *
	 * @return the selected {@link MediaServerDto} or {@code null} if none is selected
	 */
	public MediaServerDto getSelectedMediaServerDto() {
		if (selectedMediaServer == null) {
			AiConfig ai = getAiConfig();
			if (ai != null && ai.selectedServerUdn != null && !ai.selectedServerUdn.isBlank()) {
				selectedMediaServer = resolveServerByUdn(ai.selectedServerUdn);
			}
		}
		return selectedMediaServer;
	}

	private AiConfig getAiConfig() {
		if (config == null) {
			return null;
		}
		if (config.aiConfig == null) {
			config.aiConfig = new AiConfig();
		}
		return config.aiConfig;
	}

	private void persistRendererUdn(String udn) {
		AiConfig ai = getAiConfig();
		if (ai == null) {
			return;
		}
		if (Objects.equals(ai.selectedRendererUdn, udn)) {
			return;
		}
		ai.selectedRendererUdn = udn;
		writeConfigSafely();
	}

	private void persistServerUdn(String udn) {
		AiConfig ai = getAiConfig();
		if (ai == null) {
			return;
		}
		if (Objects.equals(ai.selectedServerUdn, udn)) {
			return;
		}
		ai.selectedServerUdn = udn;
		writeConfigSafely();
	}

	private void writeConfigSafely() {
		try {
			if (configPersistence != null) {
				configPersistence.writeConfig();
			}
		} catch (Exception e) {
			log.warn("Could not persist AI selection to config file: {}", e.getMessage());
		}
	}

	private MediaRendererDto resolveRendererByUdn(String udn) {
		if (udn == null || udn.isBlank()) {
			return null;
		}
		for (MediaRendererDevice dev : deviceRegistry.getAvailableMediaRenderer()) {
			if (udn.equals(dev.getUdnAsString())) {
				return dev.getAsDto();
			}
		}
		return null;
	}

	private MediaServerDto resolveServerByUdn(String udn) {
		if (udn == null || udn.isBlank()) {
			return null;
		}
		for (MediaServerDevice dev : deviceRegistry.getAvailableMediaServer()) {
			if (udn.equals(dev.getUdnAsString())) {
				return dev.getAsDto();
			}
		}
		return null;
	}

}
