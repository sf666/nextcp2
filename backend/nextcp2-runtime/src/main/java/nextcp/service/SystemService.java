package nextcp.service;

import org.springframework.stereotype.Service;
import nextcp.dto.SystemInformationDto;
import nextcp.util.CurrentVersion;

/**
 * Service layer for accessing system informations.
 */
@Service
public class SystemService
{

    private SystemInformationDto systemInformation = new SystemInformationDto();

    public SystemService()
    {
        CurrentVersion cw = new CurrentVersion();
        systemInformation.buildNumber = cw.CURRENT_VERSION;
    }

    public SystemInformationDto getSystemInformation()
    {
        return systemInformation;
    }
    
    
}
