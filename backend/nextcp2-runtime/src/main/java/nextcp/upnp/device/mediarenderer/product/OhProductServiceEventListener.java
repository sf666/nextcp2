package nextcp.upnp.device.mediarenderer.product;

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
    private IDeviceDriverCallback callback;
    
    public OhProductServiceEventListener(ApplicationEventPublisher eventPublisher, MediaRendererDevice device)
    {
        this.eventPublisher = eventPublisher;
        this.device = device;
    }

    
    public void addStandbyCallback(IDeviceDriverCallback callback)
    {
        this.callback = callback;
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
                InputSourceDto inpDto = device.getProductService().getInputSource(value);

                InputSourceChangeDto event = new InputSourceChangeDto();
                event.udn = device.getUdnAsString();
                event.inputSource = inpDto;

                eventPublisher.publishEvent(event);
            }
            catch (NullPointerException e)
            {
                log.warn("to fast ... ");
            }
        }
    }
    
    @Override
    public void standbyChange(Boolean value)
    {
        if (callback != null)
        {
            callback.standbyChanged(value);
        }
    }
}
