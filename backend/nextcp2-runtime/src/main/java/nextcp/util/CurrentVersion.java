package nextcp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CurrentVersion {

	public String CURRENT_VERSION;

	public CurrentVersion() {
		CURRENT_VERSION = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/version.txt"), StandardCharsets.UTF_8))
			.lines().collect(Collectors.joining("\n"));
	}
	
}
