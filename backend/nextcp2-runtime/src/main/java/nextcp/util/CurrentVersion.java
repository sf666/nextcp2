package nextcp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentVersion
{
	private static final Logger log = LoggerFactory.getLogger(CurrentVersion.class.getName());
	
    public String CURRENT_VERSION;

    public CurrentVersion()
    {
        InputStream is_version = getClass().getResourceAsStream("/version.txt");
        if (is_version != null)
        {
        	try (BufferedReader br = new BufferedReader(new InputStreamReader(is_version, StandardCharsets.UTF_8))) {
                CURRENT_VERSION = br.lines().collect(Collectors.joining("\n"));
        	} catch (IOException e) {
				log.error("current version error", e);
			}
        }
        else
        {
            CURRENT_VERSION = "UNKNOWN";
        }
    }
}
