package nextcp.upnp.device.mediarenderer.ohtime;

import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.dto.TrackTimeDto;
import nextcp.upnp.SubscriptionHealth;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.time1.TimeServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.time1.TimeServiceStateVariable;
import nextcp.upnp.modelGen.avopenhomeorg.time1.actions.TimeOutput;
import nextcp.util.DisplayUtils;

public class OhTimeServiceEventListener extends TimeServiceEventListenerImpl
{

    private ApplicationEventPublisher eventPublisher = null;
    private MediaRendererDevice device = null;
    private final SubscriptionHealth health = new SubscriptionHealth();

    public OhTimeServiceEventListener(ApplicationEventPublisher eventPublisher, MediaRendererDevice device)
    {
    	super(device.getDevice());
        this.eventPublisher = eventPublisher;
        this.device = device;
    }

    @Override
    public void eventProcessed()
    {
        super.eventProcessed();
        health.eventReceived();
        TimeServiceStateVariable state = getStateVariable();
        TrackTimeDto dto = generateTractTimeDto(state);

        eventPublisher.publishEvent(dto);
    }

    private TrackTimeDto generateTractTimeDto(TimeServiceStateVariable state)
    {
        return trackTime(device.getUdnAsString(), state.Seconds, state.Duration, state.TrackCount);
    }

    public boolean hasRecentEvents(long maximumAgeMs)
    {
        return health.hasRecentEvents(maximumAgeMs);
    }

    /** A snapshot does not count as a delivered GENA event, so it cannot hide a broken subscription. */
    public TrackTimeDto fromSnapshot(TimeOutput snapshot)
    {
        TimeServiceStateVariable state = getStateVariable();
        if (snapshot.Seconds != null) state.Seconds = snapshot.Seconds;
        if (snapshot.Duration != null) state.Duration = snapshot.Duration;
        if (snapshot.TrackCount != null) state.TrackCount = snapshot.TrackCount;
        return generateTractTimeDto(state);
    }

    static TrackTimeDto trackTime(String udn, Long seconds, Long duration, Long trackCount)
    {
        TrackTimeDto dto = new TrackTimeDto();
        dto.mediaRendererUdn = udn;

        dto.duration = duration == null ? 0L : Math.max(0L, duration);
        dto.seconds = seconds == null ? 0L : Math.max(0L, seconds);
        dto.trackCount = trackCount == null ? 0L : trackCount;

        dto.durationDisp = DisplayUtils.convertToDigitString(dto.duration);
        if ("00:00".equals(dto.durationDisp))
        {
            dto.streaming = true;
            if (dto.seconds > 0)
            {
                dto.durationDisp = "streaming";
            }
        }
        else
        {
            dto.streaming = false;
        }
        dto.secondsDisp = DisplayUtils.convertToDigitString(dto.seconds);
        dto.percent = calcPercent(dto.seconds, dto.duration);
        return dto;
    }

    private static int calcPercent(Long seconds, Long duration)
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
    
    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus) {
        health.failed();
    	super.ended(reason, responseStatus);
    }
    
    @Override
    public void failed(UpnpResponse responseStatus) {
        health.failed();
    	super.failed(responseStatus);
    }
}
