package nextcp.rest;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nextcp.audioaddict.AudioAddictService;
import nextcp.audioaddict.Networks;
import nextcp.audioaddict.StreamListQuality;
import nextcp.dto.AudioAddictChannelDto;
import nextcp.dto.Config;
import nextcp.dto.RadioNetwork;
import nextcp.dto.ToastrMessage;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/RadioNetworksService")
public class RestRadioNetworks {

	private static final Logger log = LoggerFactory.getLogger(RestRadioNetworks.class.getName());
	private List<RadioNetwork> networks = new ArrayList<>();

	@Autowired
	private ApplicationEventPublisher publisher = null;

	@Autowired
	private AudioAddictService audioAddictService = null;

	@Autowired
	private Config config = null;

	public RestRadioNetworks() {
		updateNetworks();
	}

	private void updateNetworks() {
		for (Networks net : Networks.values()) {
			networks.add(new RadioNetwork(net.name(), net.displayName, net.getStreamListQualityDto(), net.albumArt));
		}
		log.info("Rest service : {} available networks.", networks.size());
	}

	@GetMapping("/getNetworks")
	public List<RadioNetwork> getNetworks() {
		if (StringUtils.isAllBlank(config.audioAddictConfig.token)) {
			publisher.publishEvent(new ToastrMessage(null, "warn", "Audio Addict Radio Network", "No radio network token set."));
		}
		return networks;
	}

	@GetMapping("/getNetworkChannels/{networkAsString}")
	public List<AudioAddictChannelDto> getNetworkChannels(@PathVariable("networkAsString") String networkAsString) {
		if (StringUtils.isAllBlank(networkAsString)) {
			publisher.publishEvent(new ToastrMessage(null, "error", "Network", "Select radio network."));
			return null;
		}
		Networks network = Networks.valueOf(networkAsString);

		// TODO make quality configurable
		return audioAddictService.getChannelFor(network, StreamListQuality.MP3_320);
	}

	@PostMapping("/getFilteredChannels/{networkAsString}")
	public List<AudioAddictChannelDto> getFilteredNetworkChannels(@PathVariable("networkAsString") String networkAsString,
		@RequestBody String filter) {
		if (StringUtils.isAllBlank(networkAsString)) {
			publisher.publishEvent(new ToastrMessage(null, "error", "Network", "Select radio network."));
			return null;
		}

		Networks network = Networks.valueOf(networkAsString);

		// TODO make quality configurable
		return audioAddictService.getFilteredChannels(network, StreamListQuality.MP3_320, filter);
	}

}
