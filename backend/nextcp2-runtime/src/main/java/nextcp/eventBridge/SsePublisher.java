package nextcp.eventBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import nextcp.dto.Config;

/**
 * Global SSE Emitter class ...
 */
@Controller
public class SsePublisher
{
    private static final Logger log = LoggerFactory.getLogger(SsePublisher.class.getName());

    @Autowired
    private Config config = null;

    private final CopyOnWriteArrayList<SseEmitter> sseEmitterList = new CopyOnWriteArrayList<>();

    private ObjectMapper om = new ObjectMapper();

    // Default SSE heartbeat interval (seconds) when ApplicationConfig.sseHeartbeatSeconds is not set.
    // A heartbeat keeps long-lived SSE connections alive across reverse-proxy / load-balancer idle timeouts.
    private static final int DEFAULT_HEARTBEAT_SECONDS = 30;

    private ScheduledExecutorService heartbeatExecutor = null;

    public SsePublisher()
    {
    }

    /**
     * Start a periodic SSE heartbeat (an SSE comment frame) so long-lived connections are not dropped
     * by reverse proxies or load balancers during quiet periods. The interval is taken from
     * ApplicationConfig.sseHeartbeatSeconds; null falls back to the default, a value <= 0 disables it.
     */
    @PostConstruct
    private void startHeartbeat()
    {
        Integer configured = config.applicationConfig.sseHeartbeatSeconds;
        int seconds = (configured != null) ? configured : DEFAULT_HEARTBEAT_SECONDS;
        if (seconds <= 0)
        {
            log.info("SSE heartbeat disabled (sseHeartbeatSeconds={})", configured);
            return;
        }
        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "sse-heartbeat");
            thread.setDaemon(true);
            return thread;
        });
        heartbeatExecutor.scheduleAtFixedRate(this::sendHeartbeat, seconds, seconds, TimeUnit.SECONDS);
        log.info("SSE heartbeat enabled every {} s", seconds);
    }

    @PreDestroy
    private void stopHeartbeat()
    {
        if (heartbeatExecutor != null)
        {
            heartbeatExecutor.shutdownNow();
        }
    }

    private void sendHeartbeat()
    {
        if (sseEmitterList.isEmpty())
        {
            return;
        }
        sendToAllEmitters(SseEmitter.event().comment("heartbeat"));
    }

    /**
     * 
     * @param response
     * @return
     */
    @GetMapping("/SSE")
    public SseEmitter handle(HttpServletResponse response)
    {
        return createEmitter(response);
    }

    public void addEmitter(SseEmitter emitter)
    {
        sseEmitterList.add(emitter);
    }

    public void removeEmitter(SseEmitter emitter)
    {
        sseEmitterList.remove(emitter);
    }

    /**
     * Completes all open SSE streams. Called during an application shutdown so these long-lived
     * async requests finish immediately - otherwise the graceful web-server shutdown would wait
     * for them until its per-phase timeout (up to 30s).
     */
    public void completeAllEmitters()
    {
        for (SseEmitter emitter : sseEmitterList)
        {
            try
            {
                emitter.complete();
            }
            catch (Exception e)
            {
                // Emitter may already be dead/closed; nothing to do.
            }
        }
        sseEmitterList.clear();
    }

    protected SseEmitter createEmitter(HttpServletResponse response)
    {
        response.setHeader("Cache-Control", "no-store");

        SseEmitter emitter = new SseEmitter(config.applicationConfig.sseEmitterTimeout);
        sseEmitterList.add(emitter);

        emitter.onCompletion(() -> sseEmitterList.remove(emitter));
        emitter.onTimeout(() -> sseEmitterList.remove(emitter));

        return emitter;
    }

    /**
     * send an unnamed object
     * 
     * @param object
     */
    public void sendObject(Object object)
    {
        // not implemented yet
    }

    public void sendObjectAsJson(String name, Object object)
    {
        ObjectWriter myWriter = om.writer();
        try
        {
            String jsonString = myWriter.writeValueAsString(object);
            SseEventBuilder builder = SseEmitter.event().name(name).data(jsonString, MediaType.APPLICATION_JSON);
            sendToAllEmitters(builder);
        }
        catch (JsonProcessingException e)
        {
            log.warn("couldn't send object as json");
        }
    }

    private synchronized void sendToAllEmitters(SseEventBuilder eventBuilder)
    {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        sseEmitterList.forEach(emitter -> {
            try
            {
                emitter.send(eventBuilder);
            }
            catch (Exception e)
            {
                log.info("[sendToAllEmitters] emitter is dead : ");
                deadEmitters.add(emitter);
            }
        });

        sseEmitterList.removeAll(deadEmitters);
    }
}
