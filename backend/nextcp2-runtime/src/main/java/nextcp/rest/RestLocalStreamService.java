package nextcp.rest;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextcp.service.LocalStreamProxyService;

/**
 * Streaming endpoint for local, in-browser audio playback (the "This Device" virtual renderer).
 * <p>
 * The browser points an HTML5 {@code <audio>} element at {@code /LocalStream/stream?url=<mediaUrl>};
 * the actual pass-through, User-Agent tagging and format negotiation with the media server live in
 * {@link LocalStreamProxyService}. This controller only wires the HTTP request through.
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/LocalStream")
public class RestLocalStreamService {

	private final LocalStreamProxyService proxyService;

	public RestLocalStreamService(LocalStreamProxyService proxyService) {
		this.proxyService = proxyService;
	}

	@GetMapping("/stream")
	public void stream(@RequestParam("url") String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
		proxyService.proxy(url, request, response);
	}
}
