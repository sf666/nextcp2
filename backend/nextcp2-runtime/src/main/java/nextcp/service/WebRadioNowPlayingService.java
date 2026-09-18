package nextcp.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextcp.dto.WebStreamNowPlayingDto;

/**
 * Hands on what a continuous stream served by UMS is playing right now. Where UMS gets that from
 * differs per stream - ICY blocks for a plain internet radio, the AudioAddict API for a channel,
 * its own playback state for a curated playlist - but all of them arrive here the same way: as a
 * GENA event that {@link nextcp.upnp.device.mediaserver.UmsServerDevice} hands over. No polling.
 * <p>
 * The event names the objectID of the stream, not a renderer, and it is passed on exactly like
 * that. Whoever displays a track decides for itself whether an event is about it, which puts the
 * browser player - it has no renderer here at all - on the same path as a UPnP renderer.
 */
@Component
public class WebRadioNowPlayingService
{
    private static final Logger log = LoggerFactory.getLogger(WebRadioNowPlayingService.class.getName());

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ApplicationEventPublisher publisher;

    public WebRadioNowPlayingService(ApplicationEventPublisher publisher)
    {
        this.publisher = publisher;
    }

    /**
     * @param nowPlaying the JSON payload of the UMS "WebStreamNowPlaying" state variable.
     */
    public void onWebStreamNowPlaying(String nowPlaying)
    {
        if (StringUtils.isBlank(nowPlaying))
        {
            return;
        }
        WebStreamNowPlayingDto dto = parseNowPlaying(nowPlaying);
        if (dto == null || StringUtils.isBlank(dto.objectID))
        {
            return;
        }
        log.debug("web stream {} announces : {}", dto.objectID, dto.streamTitle);
        publisher.publishEvent(dto);
    }

    /**
     * @return NULL when the payload is not readable
     */
    public WebStreamNowPlayingDto parseNowPlaying(String nowPlaying)
    {
        if (StringUtils.isBlank(nowPlaying))
        {
            return null;
        }
        try
        {
            JsonNode node = OBJECT_MAPPER.readTree(nowPlaying);
            return new WebStreamNowPlayingDto(
                node.path("objectID").asText(""),
                StringUtils.trimToEmpty(node.path("streamTitle").asText("")),
                StringUtils.trimToEmpty(node.path("artist").asText("")),
                StringUtils.trimToEmpty(node.path("title").asText("")),
                StringUtils.trimToEmpty(node.path("artUrl").asText("")));
        }
        catch (Exception e)
        {
            log.warn("cannot parse now-playing event : {}", nowPlaying, e);
            return null;
        }
    }
}
