package nextcp.rest;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nextcp.audioaddict.AudioAddictServiceConfig;
import nextcp.audioaddict.Networks;
import nextcp.dto.Config;
import nextcp.dto.RadioNetwork;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/RadioNetworksService")
public class RestRadioNetworks {

	private static final Logger log = LoggerFactory.getLogger(RestRadioNetworks.class.getName());
	private List<RadioNetwork> networks = new ArrayList<>();

	@Autowired
    private Config config = null;

	public RestRadioNetworks() {
		updateNetworks();
	}

	private void updateNetworks() {
		for (Networks net : Networks.values()) {
			networks.add(new RadioNetwork(net.name(), net.displayName, net.getStreamListQualityDto()));
		}
		log.info("Rest service : {} available networks.", networks.size());
	}

	@GetMapping(name = "getNetworks")
	public List<RadioNetwork> getNetworks() {
		return networks;
	}
	
	@Bean
	public AudioAddictServiceConfig createAudioAddictConfig() {
		AudioAddictServiceConfig aac = new AudioAddictServiceConfig();
		aac.preferEuropeanServer = config.applicationConfig.audioAddictPreferEuropeanServer;
		aac.token = config.applicationConfig.audioAddictToken;
		return new AudioAddictServiceConfig();
	}
	
}
