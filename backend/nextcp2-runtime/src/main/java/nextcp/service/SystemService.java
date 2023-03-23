package nextcp.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import nextcp.dto.Config;
import nextcp.dto.SystemInformationDto;
import nextcp.dto.ToastrMessage;
import nextcp.util.CurrentVersion;

/**
 * Service layer for accessing system informations.
 */
@Service
public class SystemService
{
    private static final Logger log = LoggerFactory.getLogger(SystemService.class.getName());

    private SystemInformationDto systemInformation = new SystemInformationDto();

    @Autowired
    private Config config = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    public SystemService()
    {
        CurrentVersion cw = new CurrentVersion();
        systemInformation.buildNumber = cw.CURRENT_VERSION;
    }

    public SystemInformationDto getSystemInformation()
    {
        return systemInformation;
    }

    public void restartNextcp()
    {
        if (StringUtils.isAllBlank(config.applicationConfig.pathToRestartScript))
        {
            publisher.publishEvent(new ToastrMessage(null, "error", "Restart", "No restart script set. Go to settings and set location of restart script."));
            return;
        }

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        Process process;
        try
        {
            if (isWindows)
            {
                String exec = String.format("cmd.exe /c %s", config.applicationConfig.pathToRestartScript);
                log.info("restart > exec on windows : " + exec);
                process = Runtime.getRuntime().exec(exec);
            }
            else
            {
                String exec = String.format("/bin/sh -c %s", config.applicationConfig.pathToRestartScript);
                log.info("restart > exec on unix : " + exec);
                process = Runtime.getRuntime().exec(exec);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0)
            {
                log.info("restart failed with exit code : " + exitCode);
                publisher.publishEvent(
                        new ToastrMessage(null, "error", "Restart", "Process finished with exit code " + exitCode + ". The restart of the system has probably failed."));
            }
            else
            {
                log.info("restart completed.");
            }
        }
        catch (IOException e)
        {
            log.error("exec", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "Restart", String.format("couldn't exec script %s", config.applicationConfig.pathToRestartScript)));
        }
        catch (InterruptedException e)
        {
            log.error("exec", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "Restart", "Process didn't finish in time. The restart of the system has probably failed."));
        }
    }

}
