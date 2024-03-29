package nextcp.upnp.device.mediarenderer.product;

import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.dto.InputSourceChangeDto;
import nextcp.dto.InputSourceDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.product1.ProductServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.product1.ProductServiceStateVariable;

public class OhProductServiceEventListener extends ProductServiceEventListenerImpl
{
    private static final Logger log = LoggerFactory.getLogger(OhProductServiceEventListener.class.getName());

    private ApplicationEventPublisher eventPublisher = null;
    private MediaRendererDevice device = null;
    private IDeviceDriverCallback standbyCallback;
    private ISourceChangedCallback sourceChangedCallback;
    
    public OhProductServiceEventListener(ApplicationEventPublisher eventPublisher, MediaRendererDevice device)
    {
    	super(device.getDevice());
        this.eventPublisher = eventPublisher;
        this.device = device;
    }

    
    public void addStandbyCallback(IDeviceDriverCallback callback)
    {
        this.standbyCallback = callback;
    }
    
    public void setSourceChangedCallback(ISourceChangedCallback callback)
    {
        this.sourceChangedCallback = callback;
    }
    
    @Override
    public void eventProcessed()
    {
        super.eventProcessed();
        ProductServiceStateVariable state = getStateVariable();

        log.debug(String.format("State : %s", state.toString()));
    }

    @Override
    public void sourceIndexChange(Long value)
    {
        super.sourceIndexChange(value);
        if (value != null)
        {
            try
            {
                if (device.getProductService() != null)
                {
                    InputSourceDto inpDto = device.getProductService().getInputSource(value);
                    
                    InputSourceChangeDto event = new InputSourceChangeDto();
                    event.udn = device.getUdnAsString();
                    event.inputSource = inpDto;

                    eventPublisher.publishEvent(event);
                    
                    if (sourceChangedCallback != null)
                    {
                        sourceChangedCallback.sourceChanged(inpDto);
                    }
                }
                else
                {
                    log.warn("product service not yet initialized.");
                }
            }
            catch (NullPointerException e)
            {
                log.warn("to fast ... ", e);
            }
        }
    }
    
    @Override
    public void standbyChange(Boolean value)
    {
        if (standbyCallback != null)
        {
            standbyCallback.standbyChanged(value);
        }
    }
    
    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus) {
    	super.ended(reason, responseStatus);
        device.setServicesEnded(true);            	
    }
    
    @Override
    public void failed(UpnpResponse responseStatus) {
    	super.failed(responseStatus);
    	device.setServicesEnded(true);    	
    }
}
