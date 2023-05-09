package nextcp.eventBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

    public SsePublisher()
    {
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

    private void sendToAllEmitters(SseEventBuilder eventBuilder)
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
