package nextcp.upnp.device.mediarenderer.avtransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.dto.TrackInfoDto;
import nextcp.dto.TransportServiceStateDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.avtransport.event.AvTransportStateChangedEvent;

public class AvTransportEventPublisher extends BaseAvTransportChangeEventImpl
{
    private static final Logger log = LoggerFactory.getLogger(AvTransportEventPublisher.class.getName());
    
    private MediaRendererDevice device = null;
    private boolean shouldPublishTransportServiceState = false;
    
    private AvTransportState currentAvTransportState = new AvTransportState(); // Init with empty state object 


    public AvTransportEventPublisher(MediaRendererDevice device)
    {
        this.device = device;
    }

    private ApplicationEventPublisher getEventPublisher()
    {
        return device.getEventPublisher();
    }

    @Override
    public void processingFinished(AvTransportState currentAvTransportState)
    {
        this.currentAvTransportState = currentAvTransportState;
        if (log.isDebugEnabled())
        {
            log.debug("AVTransportState : " + currentAvTransportState);
        }
        publishAllAvEvents();
    }

    public void publishAllAvEvents()
    {        
        publishGlobalAvTransportState(currentAvTransportState);
         
        if (!device.hasOhInfoService())
        {
            getEventPublisher().publishEvent(getAsTrackInfo(currentAvTransportState));
        }
    }

    private void publishGlobalAvTransportState(AvTransportState currentAvTransportState)
    {
        AvTransportStateChangedEvent event = new AvTransportStateChangedEvent();
        event.state = currentAvTransportState;
        event.device = device;
        log.debug("publishing AvTransportStateChangedEvent for device {}" , event.device.getFriendlyName());
        getEventPublisher().publishEvent(event);
        
        if (shouldPublishTransportServiceState)
        {
            TransportServiceStateDto dto = device.getTransportServiceBridge().getCurrentTransportServiceState();
            dto.udn = device.getUdnAsString();
            
            log.debug("publishing TransportServiceStateDto : " + dto);
            device.getEventPublisher().publishEvent(dto);
        }
        else
        {
            log.debug("shouldPublishTransportServiceState is false.");
        }
    }

    private TrackInfoDto getAsTrackInfo(AvTransportState transportState)
    {
        TrackInfoDto dto = new TrackInfoDto();

        dto.mediaRendererUdn = device.getUdnAsString();
        dto.metatext = transportState.CurrentTrackMetaData;
        dto.uri = transportState.CurrentTrackURI;
        dto.currentTrack = device.getDtoBuilder().extractXmlAsMusicItem(transportState.CurrentTrackMetaData);

        return dto;
    }
    
    public AvTransportState getCurrentAvTransportState()
    {
        return currentAvTransportState;
    }

    public boolean isShouldPublishTransportServiceState()
    {
        return shouldPublishTransportServiceState;
    }

    public void setShouldPublishTransportServiceState(boolean shouldPublishTransportServiceState)
    {
        this.shouldPublishTransportServiceState = shouldPublishTransportServiceState;
    }
    
    
}
