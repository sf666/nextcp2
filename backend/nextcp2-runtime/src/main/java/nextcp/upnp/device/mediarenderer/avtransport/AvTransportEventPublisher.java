package nextcp.upnp.device.mediarenderer.avtransport;

import org.springframework.context.ApplicationEventPublisher;

import nextcp.dto.TrackInfoDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.avtransport.event.AvTransportStateChangedEvent;

public class AvTransportEventPublisher extends BaseAvTransportChangeEventImpl
{
    private MediaRendererDevice device = null;
    
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
        getEventPublisher().publishEvent(event);
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
    
}
