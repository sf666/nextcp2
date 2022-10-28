package nextcp.upnp.device.mediarenderer;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.dto.InputSourceDto;
import nextcp.upnp.device.mediarenderer.product.ISourceChangedCallback;

/**
 * Does some Lumin fixes. OpenHome Transprt Service is not doing what it should. Transport state is not correctly published. Therefore the transpot state has to be read from the
 * current active source.
 */
public class LuminDevice extends MediaRendererDevice implements ISourceChangedCallback
{
    private static final Logger log = LoggerFactory.getLogger(LuminDevice.class.getName());

    public LuminDevice(RemoteDevice device)
    {
        super(device);
    }

    @Override
    protected void init()
    {
        super.init();
        ohProductServiceEventListener.setSourceChangedCallback(this);
        
        // Do not use TransportService information. not working on Lumin (checked with firmware 15.0)
        ohTransportEventListener.setShouldPublishTransportServiceState(false);
        InputSourceDto currentSource = productService.getCurrentInputSource();
        sourceChanged(currentSource);
    }

    @Override
    public void sourceChanged(InputSourceDto source)
    {
        disableEventListener();
        

        if ("Radio".equalsIgnoreCase(source.Type))
        {
            transportBridge = oh_radioBridge;
            ohRadioServiceEventListener.setShouldPublishTransportServiceState(true);
        }
        else if ("Playlist".equalsIgnoreCase(source.Type))
        {
            transportBridge = oh_playlistBridge;
            ohPlaylistServiceEventListener.setShouldPublishTransportServiceState(true);
        }
        else if ("UpnpAv".equalsIgnoreCase(source.Type))
        {
            transportBridge = avTransportBridge;
            avTransportEventPublisher.setShouldPublishTransportServiceState(true);
        }
        
        getEventPublisher().publishEvent(transportBridge.getCurrentTransportServiceState());
    }

    private void disableEventListener()
    {
        if (ohRadioServiceEventListener != null) 
        {
            ohRadioServiceEventListener.setShouldPublishTransportServiceState(false);
        }
        if (ohPlaylistServiceEventListener != null) 
        {
            ohPlaylistServiceEventListener.setShouldPublishTransportServiceState(false);
        }
        if (avTransportEventPublisher != null) 
        {
            avTransportEventPublisher.setShouldPublishTransportServiceState(false);
        }
    }
}
