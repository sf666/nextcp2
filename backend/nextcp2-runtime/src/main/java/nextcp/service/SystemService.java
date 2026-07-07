package nextcp.service;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
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
    private CurrentVersion cw = new CurrentVersion();

    @Autowired
    private Config config = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    @Autowired
    private ConfigurableApplicationContext appContext = null;

    @Autowired
    private nextcp.eventBridge.SsePublisher ssePublisher = null;

    /**
     * System property that marks the native desktop app (set by package/build-native.sh).
     * Desktop-only features such as the in-app shutdown are gated on this, so a server / NAS /
     * Docker deployment cannot be shut down from the web UI by anyone on the LAN.
     */
    private static final String DESKTOP_MODE_PROPERTY = "nextcp.desktopMode";

    public SystemService()
    {
    }

    public SystemInformationDto getSystemInformation()
    {
        return cw.getVersion();
    }

    /**
     * @return {@code true} when running as the native desktop app.
     */
    public boolean isDesktopMode()
    {
        return Boolean.parseBoolean(System.getProperty(DESKTOP_MODE_PROPERTY, "false"));
    }

    /**
     * Gracefully shuts down the application. Only permitted in desktop mode; a request in any
     * other deployment is refused so the server cannot be stopped from the web UI. The JVM exit
     * runs on a short-delayed background thread so the HTTP response can still be delivered first.
     */
    public void shutdownNextcp()
    {
        if (!isDesktopMode())
        {
            log.warn("Shutdown requested but not running in desktop mode - ignoring.");
            publisher.publishEvent(new ToastrMessage(null, "error", "Shutdown", "Shutdown is only available in desktop mode."));
            return;
        }
        log.info("Shutdown requested via UI (desktop mode). Terminating application ...");
        Thread t = new Thread(() ->
        {
            try
            {
                // Give the REST call time to return before the context closes.
                Thread.sleep(700);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            // Complete the long-lived SSE streams first; otherwise the graceful web-server
            // shutdown treats them as active requests and waits the full per-phase timeout (~30s).
            try
            {
                ssePublisher.completeAllEmitters();
            }
            catch (Exception e)
            {
                log.warn("Error completing SSE emitters during shutdown: {}", e.getMessage());
            }
            // Backstop: if the orderly shutdown still stalls (e.g. another long-lived stream),
            // force termination so the desktop quit stays responsive.
            Thread watchdog = new Thread(() ->
            {
                try
                {
                    Thread.sleep(10_000);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
                log.warn("Shutdown still not complete after 10s - forcing exit.");
                Runtime.getRuntime().halt(0);
            }, "nextcp2-shutdown-watchdog");
            watchdog.setDaemon(true);
            watchdog.start();
            int exitCode = SpringApplication.exit(appContext, () -> 0);
            System.exit(exitCode);
        }, "nextcp2-shutdown");
        t.start();
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
        ProcessBuilder pb;
        try
        {
            if (isWindows)
            {
                log.info("restart > exec on windows : " + String.format("cmd.exe /c %s", config.applicationConfig.pathToRestartScript));
                pb = new ProcessBuilder("cmd.exe", "/c", config.applicationConfig.pathToRestartScript);

            }
            else
            {
                log.info("restart > exec on unix : " + String.format("/bin/sh -c %s", config.applicationConfig.pathToRestartScript));
                pb = new ProcessBuilder("/bin/sh", "-c", config.applicationConfig.pathToRestartScript);
            }

            pb.redirectErrorStream(true);
            process = pb.start();
            log.info(new String(IOUtils.toByteArray(process.getInputStream())));
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
                publisher.publishEvent(new ToastrMessage(null, "info", "Restart", "Restart successful. Please reload current page."));
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
