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
import nextcp.dto.MusicItemDto;
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

	private Collection<MusicItemDto> availableMediaRendererRadioStations = null;
	
	private MediaRendererDevice selectedMediaRenderer = null;

	private MediaServerDevice selectedMediaServer = null;

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
			MediaRendererDevice device = resolveRendererByUdn(ai.selectedRendererUdn);
			if (device != null) {
				setSelectedMediaRenderer(device);
				log.info("Restored selected Media Renderer from config: udn={}, friendlyName={}", device.getUdnAsString(), device.getFriendlyName());
			} else {
				log.info("Persisted Media Renderer UDN {} not yet available in registry; will resolve lazily", ai.selectedRendererUdn);
			}
		}
		if (ai.selectedServerUdn != null && !ai.selectedServerUdn.isBlank()) {
			MediaServerDevice device = resolveServerByUdn(ai.selectedServerUdn);
			if (device != null) {
				selectedMediaServer = device;
				log.info("Restored selected Media Server from config: udn={}, friendlyName={}", device.getUdnAsString(), device.getFriendlyName());
			} else {
				log.info("Persisted Media Server UDN {} not yet available in registry; will resolve lazily", ai.selectedServerUdn);
			}
		}
	}

	private void setSelectedMediaRenderer(MediaRendererDevice device) {
		this.selectedMediaRenderer = device;
		if (selectedMediaRenderer.hasOhPlaylistService()) {
			log.info("Selected Media Renderer '{}' supports OpenHome Playlist Service. Reading station names ... ", device.getFriendlyName());
			availableMediaRendererRadioStations = selectedMediaRenderer.getRadioServiceBridge().getRadioStations();
		}
	}

	@Tool(name = "list_renderer_radio_station", description = """
		Returns a list of available Media Renderer Radio Stations. Use this tool to collect preset readio stations from the currently selected
		Media Renderer. The station names can then be used to play a station via the "play_radio_on_renderer" tool.
		
		This tool can return NULL or an empty list, if no preset radio stations are available on the selected Media Renderer.
		In that case, use the "play_radio_on_renderer" tool to play a station by name without preset.
			""")
	public Collection<MusicItemDto> listMediaRendererRadioStation() {
		log.info("UPnP command received: list_renderer_radio_station. List Radio Stations provided by media renderer.");
		
		if (availableMediaRendererRadioStations == null) {
			log.info("No radio stations available from media renderer.");
		}
		return availableMediaRendererRadioStations;
	}

		
	// Renderer
	//
	@Tool(name = "list_renderers", description = """
		Returns a list of available Media Renderer UPnP devices (also called players) which are online.

		Do not use this tool for getting the active or currently selected Media Renderer.
			""")
	public Collection<MediaRendererDto> listMediaRenderer() {
		log.info("UPnP command received: list_renderers. List all available Media Renderer");

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
		log.info("UPnP command received: selected_renderer. Get selected Media Renderer");
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
		log.info("UPnP command received: select_renderer. Select Media Renderer - {}", udn);
		var found = deviceRegistry.getAvailableMediaRenderer().stream().filter(renderer -> renderer.getUdnAsString().equals(udn))
			.findFirst();

		if (found.isPresent()) {
			selectedMediaRenderer = found.get();
			persistRendererUdn(selectedMediaRenderer.getUdnAsString());
			return messageSource.getMessage("mcp.device.renderer.select.response", new Object[] { selectedMediaRenderer.getFriendlyName() }, mcpLocale.getCurrentLocale());
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
		log.info("Command received: list_server. List all available Media Server");

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
		log.info("UPnP command received: selected_server. Deliver selected Media Server ...");

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
			selectedMediaServer = found.get();
			persistServerUdn(selectedMediaServer.getUdnAsString());
			return messageSource.getMessage("mcp.device.server.select.response", new Object[] { selectedMediaServer.getFriendlyName() }, mcpLocale.getCurrentLocale());
		} else {
			// Unknown UDN; keep existing selection intact.
			return messageSource.getMessage("mcp.device.server.select.notFound.response", new Object[] { udn },
				mcpLocale.getCurrentLocale());
		}
	}

	@Tool(name = "play_radio_on_renderer", description = """
		Plays a radio station on the selected Media Renderer. 
		  - Don't call this tool, if no media renderer is selected yet 
		    - use get_selected_renderer to check if a renderer is already selected
		    - use the "select_renderer" tool first to select a renderer
		    
		  - Don't call this tool, if no media server is selected yet 
		    - use selected_server to check if a server is already selected
		    - use the "select_server" tool first to select a server

		Use this tool when:
	      - The user asks to play a specific radio station which doen't matches to a preset on the selected Media Renderer.
	""")
	public String playRadioStation(@ToolParam(description = "The radio station to play by station name. ", required = true) String stationName) {
		log.info("Command received: play_radio_on_renderer. Play radio station {} from media server.", stationName);
		return "ok";
	}
	
	@Tool(name = "play_preset_radio_on_renderer", description = """
			Plays a preset radio station on the selected Media Renderer. Get alle available preset radio stations via the "list_renderer_radio_station" tool first.
			If no presets are available, use the "play_radio_on_renderer" tool to play a station by name.
			
		 	- Don't call this tool, if no media renderer is selected yet 
			    - use get_selected_renderer to check if a renderer is already selected
				- use the "select_renderer" tool first to select a renderer
			
			  
			Use this tool when:
		      - The user asks to play a specific radio station which matches to a preset on the selected Media Renderer.
		""")
	public String playRadioStationPreset(@ToolParam(description = "The preset object of the radio station to play. ", required = true) MusicItemDto station) {
		log.info("Command received: play_preset_radio_on_renderer - {}", station.title);
		if (selectedMediaRenderer.hasOhPlaylistService()) {
			log.info("Request : Playing preset radio station '{}'", station.title);
			return "ok";
		}
		return "ok";
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
		return selectedMediaRenderer == null ? null : selectedMediaRenderer.getAsDto();
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
		return selectedMediaServer == null ? null : selectedMediaServer.getAsDto();
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

	private MediaRendererDevice resolveRendererByUdn(String udn) {
		if (udn == null || udn.isBlank()) {
			return null;
		}
		for (MediaRendererDevice dev : deviceRegistry.getAvailableMediaRenderer()) {
			if (udn.equals(dev.getUdnAsString())) {
				return dev;
			}
		}
		return null;
	}

	private MediaServerDevice resolveServerByUdn(String udn) {
		if (udn == null || udn.isBlank()) {
			return null;
		}
		for (MediaServerDevice dev : deviceRegistry.getAvailableMediaServer()) {
			if (udn.equals(dev.getUdnAsString())) {
				return dev;
			}
		}
		return null;
	}

}
