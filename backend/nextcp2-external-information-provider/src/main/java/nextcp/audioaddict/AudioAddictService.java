package nextcp.audioaddict;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import nextcp.dto.AudioAddictChannelDto;

@Service
public class AudioAddictService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AudioAddictService.class.getName());
	private AudioAddictServiceConfig config = null;

	private ConcurrentHashMap<Platform, RadioNetwork> networkSettings = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
//		for (Platform network : Platform.values()) {
//			getNetwork(network);
//		}
	}

	@Autowired
	public AudioAddictService(AudioAddictServiceConfig config) {
		this.config = config;
	}

	public void updateConfig(AudioAddictServiceConfig config) {
		this.config = config;
		for (Platform nw : networkSettings.keySet()) {
			networkSettings.get(nw).updateConfig(config);
		}		
	}

	public List<AudioAddictChannelDto> getChannelFor(Platform platform) {
		RadioNetwork network = getNetwork(platform);
		return network.getChannel();
	}

	private RadioNetwork getNetwork(Platform platform) {
		RadioNetwork network = networkSettings.get(platform);

		if (network == null) {
			network = new RadioNetwork(platform, config);
			networkSettings.put(platform, network);
		}
		return network;
	}

	public List<AudioAddictChannelDto> getFilteredChannels(Platform platform, String filter) {
		RadioNetwork network = getNetwork(platform);
		return network.getFilteredChannels(filter);
	}

	public List<String> getFiltersForNetwork(Platform platform) {
		RadioNetwork network = getNetwork(platform);
		return network.getFilters();
	}
}
