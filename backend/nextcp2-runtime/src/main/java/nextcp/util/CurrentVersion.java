package nextcp.util;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.dto.SystemInformationDto;

public class CurrentVersion
{
	private static final Logger log = LoggerFactory.getLogger(CurrentVersion.class.getName());

	// Maven's ${maven.build.timestamp} is always rendered in UTC, regardless of the
	// build machine's timezone. Re-interpret it as UTC and convert to the JVM's
	// (i.e. the running container's) default zone before exposing it.
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public String CURRENT_VERSION;
    private SystemInformationDto version = new SystemInformationDto();
    private Properties p = new Properties();

    public CurrentVersion()
    {
        InputStream is_version = getClass().getResourceAsStream("/version.txt");
        if (is_version != null)
        {
        	try {
				p.load(is_version);
				version.buildNumber = p.getProperty("commit");
				version.time = toLocalBuildTime(p.getProperty("timestamp"));
				version.name = p.getProperty("version");
			} catch (IOException e) {
				log.error("cannot read version file.");
			}
        }
    }

    private String toLocalBuildTime(String utcTimestamp)
    {
        if (utcTimestamp == null)
        {
            return null;
        }
        try
        {
            LocalDateTime utc = LocalDateTime.parse(utcTimestamp, TIMESTAMP_FORMAT);
            return utc.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).format(TIMESTAMP_FORMAT);
        }
        catch (Exception e)
        {
            log.warn("cannot parse build timestamp '{}', returning as-is.", utcTimestamp);
            return utcTimestamp;
        }
    }

    public SystemInformationDto getVersion() {
    	return version;
    }
}
