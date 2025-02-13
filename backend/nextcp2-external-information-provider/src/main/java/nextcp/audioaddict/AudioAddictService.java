package nextcp.audioaddict;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nextcp.dto.AudioAddictChannelDto;

@Service
public class AudioAddictService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AudioAddictService.class.getName());
	private AudioAddictServiceConfig config = null;
	
	private ConcurrentHashMap<Networks, Network> networkSettings = new ConcurrentHashMap<>();

	@Autowired
	public AudioAddictService(AudioAddictServiceConfig config) {
		this.config = config;
	}

	public List<AudioAddictChannelDto> getChannelFor(Networks platform, StreamListQuality quality) {
		Network network = networkSettings.get(platform);
		
		if (network == null) {
			LOGGER.debug("initialize network : " + platform.displayName);
			network = new Network(platform, quality, config);
			networkSettings.put(platform, network);
		}
		return network.getChannel();
	}
}
