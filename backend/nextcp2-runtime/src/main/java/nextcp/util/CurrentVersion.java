package nextcp.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CurrentVersion
{

    public String CURRENT_VERSION;

    public CurrentVersion()
    {
        InputStream is_version = getClass().getResourceAsStream("/version.txt");
        if (is_version != null)
        {
            CURRENT_VERSION = new BufferedReader(new InputStreamReader(is_version, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        }
        else
        {
            CURRENT_VERSION = "UNKNOWN";
        }
    }
}
