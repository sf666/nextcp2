package nextcp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.dto.SystemInformationDto;

public class CurrentVersion
{
	private static final Logger log = LoggerFactory.getLogger(CurrentVersion.class.getName());
	
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
				version.time = p.getProperty("timestamp");
				version.name = p.getProperty("version");
			} catch (IOException e) {
				log.error("cannot read version file.");
			}
        }
    }
    
    public SystemInformationDto getVersion() {
    	return version;
    }
}
