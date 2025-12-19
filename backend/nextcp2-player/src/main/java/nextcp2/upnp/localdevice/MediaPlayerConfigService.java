package nextcp2.upnp.localdevice;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;
import nextcp.db.service.BasicDbService;
import nextcp.db.service.KeyValuePair;
import nextcp.dto.ContainerIdDto;
import nextcp.dto.MediaPlayerConfigDto;
import nextcp2.upnp.localdevice.LocalDeviceConfig.FileType;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

@Service
public class MediaPlayerConfigService implements ILocalDeviceConfigProducer {

	private static final Logger log = LoggerFactory.getLogger(MediaPlayerConfigService.class.getName());
	private static final String CONFIG_KEY_MEDIA_PLAYER = "MEDIAPLAYER_CONFIG";

	private JsonMapper om = JsonMapper.builder().build();

	private BasicDbService dbService = null;
	
	private MediaPlayerConfigDto mediaPlayerConfig = null;
	private LocalDeviceConfig ldc = null;

	
	public MediaPlayerConfigDto getMediaPlayerConfigDto() {
		return mediaPlayerConfig;
	}

	@Autowired
	public MediaPlayerConfigService(BasicDbService dbService) {
		this.dbService = dbService;
		mediaPlayerConfig = readConfig();
		createCurrentLocalDeviceConfig();
	}

	public void writeConfig() {
		try {
			ObjectWriter writer = om.writer();
			String value = writer.withDefaultPrettyPrinter().writeValueAsString(mediaPlayerConfig);
			dbService.updateJsonStoreValue(new KeyValuePair(CONFIG_KEY_MEDIA_PLAYER, value));
		} catch (Exception e) {
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
		} catch (Exception e) {
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
		c.mediaServerUdn = "";
		return c;
	}
	
	public void updateConfig(MediaPlayerConfigDto configDto) {
		mediaPlayerConfig = configDto;
		createCurrentLocalDeviceConfig();
		writeConfig();
	}

	private void createCurrentLocalDeviceConfig() {
		ldc = new LocalDeviceConfig(mediaPlayerConfig.workdir, FileType.valueOf(mediaPlayerConfig.playType), mediaPlayerConfig.overwrite, mediaPlayerConfig.script);
	}
	
	
	@Override
	public LocalDeviceConfig produceCurrentLocalDeviceConfig() {
		return ldc;
	}
}
