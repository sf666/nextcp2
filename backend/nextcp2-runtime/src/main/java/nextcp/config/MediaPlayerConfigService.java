package nextcp.config;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import nextcp.db.service.BasicDbService;
import nextcp.db.service.KeyValuePair;
import nextcp.dto.ContainerIdDto;
import nextcp.dto.MediaPlayerConfigDto;
import nextcp.eventBridge.SsePublisher;
import nextcp2.upnp.localdevice.ILocalDeviceConfigProducer;
import nextcp2.upnp.localdevice.LocalDeviceConfig;
import nextcp2.upnp.localdevice.LocalDeviceConfig.FileType;

@Service
public class MediaPlayerConfigService implements ILocalDeviceConfigProducer {

	private static final Logger log = LoggerFactory.getLogger(MediaPlayerConfigService.class.getName());
	private static final String CONFIG_KEY_MEDIA_PLAYER = "MEDIAPLAYER_CONFIG";

	private ObjectMapper om = new ObjectMapper();
	// public static final String SERVER_CONFIG_QUEUENAME =
	// "SERVER_DEVICES_CONFIG_CHANGED";

	private BasicDbService dbService = null;
	private SsePublisher ssePublisher = null;
	
	private MediaPlayerConfigDto ldc = null;

	
	public MediaPlayerConfigDto getMediaPlayerConfigDto() {
		return ldc;
	}

	@Autowired
	public MediaPlayerConfigService(BasicDbService dbService, SsePublisher ssePublisher) {
		this.dbService = dbService;
		this.ssePublisher = ssePublisher;
		ldc = readConfig();
	}

	public void writeConfig() {
		try {
			ObjectWriter writer = om.writer();
			String value = writer.withDefaultPrettyPrinter().writeValueAsString(ldc);
			dbService.updateJsonStoreValue(new KeyValuePair(CONFIG_KEY_MEDIA_PLAYER, value));
		} catch (JsonProcessingException e) {
			log.error("could not write config", e);
		}
	}

	private MediaPlayerConfigDto readConfig() {
		try {
			String value = "";
			value = dbService.selectJsonStoreValue(CONFIG_KEY_MEDIA_PLAYER);
			MediaPlayerConfigDto renderer = readConfig(value);
			return renderer;
		} catch (Exception e) {
			log.error("readConfig", e);
			return generateDefaultConfig();
		}
	}

	private MediaPlayerConfigDto readConfig(String json) {
		if (StringUtils.isAllBlank(json)) {
			return generateDefaultConfig();
		}
		try {
			return om.readValue(json, MediaPlayerConfigDto.class);
		} catch (JsonParseException e) {
			log.error("error in config file. JSON text could not be parsed.", e);
		} catch (IOException e) {
			log.error("could not read config", e);
		}
		return generateDefaultConfig();
	}

	
	private MediaPlayerConfigDto generateDefaultConfig() {
		MediaPlayerConfigDto c = new MediaPlayerConfigDto();
		c.playType = "ALBUM";
		c.overwrite = false;
		c.script = "";
		c.workdir = "";
		c.addToFolderId = new ContainerIdDto("","");
		c.addToPlaylist = false;
		c.addToPlaylistId = new ContainerIdDto("","");
		return c;
	}
	
	public void updateConfig(MediaPlayerConfigDto configDto) {
		ldc = configDto;
		writeConfig();
	}
	
	@Override
	public LocalDeviceConfig produceCurrentLocalDeviceConfig() {
		return new LocalDeviceConfig(ldc.workdir, FileType.valueOf(ldc.playType), ldc.overwrite, ldc.script);
	}
}
