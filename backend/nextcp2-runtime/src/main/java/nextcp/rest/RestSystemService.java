package nextcp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nextcp.dto.SystemInformationDto;
import nextcp.service.SystemService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/SystemService")
public class RestSystemService {

	@Autowired
	private SystemService systemService = null;

	public RestSystemService() {
	}

	@GetMapping("/getSystemInformation")
	public SystemInformationDto getSystemInformation() {
		return systemService.getSystemInformation();
	}
}
