package nextcp.upnp.device.mediarenderer.ohradio;

import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import nextcp.dto.TransportServiceStateDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.RadioServiceEventListenerImpl;

public class OhRadioServiceEventListener extends RadioServiceEventListenerImpl
{
    private MediaRendererDevice device = null;

    private boolean shouldPublishTransportServiceState = false;

    public OhRadioServiceEventListener(MediaRendererDevice device)
    {
        this(device, true);
    }

    public OhRadioServiceEventListener(MediaRendererDevice device, boolean shouldPublishTransportServiceState)
    {
    	super(device.getDevice());
        this.device = device;
        this.shouldPublishTransportServiceState = shouldPublishTransportServiceState;
    }

    @Override
    public void eventProcessed()
    {
        publishState();
    }

    private void publishState()
    {
        if (!shouldPublishTransportServiceState)
        {
            return;
        }
        
        TransportServiceStateDto dto = new TransportServiceStateDto();

        dto.udn = device.getUdnAsString();
        dto.canPause = true;
        dto.canRepeat = false;
        dto.canSeek = false;
        dto.canShuffle = false;
        dto.canSkipNext = false;
        dto.canSkipPrevious = false;
        dto.transportState = getStateVariable().TransportState;

        device.getEventPublisher().publishEvent(dto);
    }

    public boolean isShouldPublishTransportServiceState()
    {
        return shouldPublishTransportServiceState;
    }

    public void setShouldPublishTransportServiceState(boolean shouldPublishTransportServiceState)
    {
        this.shouldPublishTransportServiceState = shouldPublishTransportServiceState;
    }
    
    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus) {
    	super.ended(reason, responseStatus);
        device.setServicesEnded(true);            	
    }
}
