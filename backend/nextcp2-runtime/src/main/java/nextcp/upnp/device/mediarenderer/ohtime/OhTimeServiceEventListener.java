package nextcp.upnp.device.mediarenderer.ohtime;

import org.springframework.context.ApplicationEventPublisher;

import nextcp.dto.TrackTimeDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.time.TimeServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.time.TimeServiceStateVariable;
import nextcp.util.DisplayUtils;

public class OhTimeServiceEventListener extends TimeServiceEventListenerImpl
{

    private ApplicationEventPublisher eventPublisher = null;
    private MediaRendererDevice device = null;

    public OhTimeServiceEventListener(ApplicationEventPublisher eventPublisher, MediaRendererDevice device)
    {
        this.eventPublisher = eventPublisher;
        this.device = device;
    }

    @Override
    public void eventProcessed()
    {
        super.eventProcessed();
        TimeServiceStateVariable state = getStateVariable();
        TrackTimeDto dto = generateTractTimeDto(state);

        eventPublisher.publishEvent(dto);
    }

    private TrackTimeDto generateTractTimeDto(TimeServiceStateVariable state)
    {
        TrackTimeDto dto = new TrackTimeDto();
        dto.mediaRendererUdn = device.getUDN().getIdentifierString();

        dto.duration = state.Duration;
        dto.seconds = state.Seconds;
        dto.trackCount = state.TrackCount;

        dto.durationDisp = DisplayUtils.convertToDigitString(state.Duration);
        if ("00:00".equals(dto.durationDisp))
        {
            dto.streaming = true;
            if (state.Seconds != null && state.Seconds > 0)
            {
                dto.durationDisp = "streaming";
            }
        }
        else
        {
            dto.streaming = false;
        }
        dto.secondsDisp = DisplayUtils.convertToDigitString(state.Seconds);
        dto.percent = calcPercent(state.Seconds, state.Duration);
        return dto;
    }

    private int calcPercent(Long seconds, Long duration)
    {
        if (seconds == null || duration == null)
        {
            return 0;
        }

        if (duration == 0)
        {
            return 0;
        }

        return (int) ((seconds * 100) / duration);
    }
}
