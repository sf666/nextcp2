package nextcp.upnp.device.mediarenderer.product;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.services.IProductService;
import nextcp.dto.InputSourceDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.OpenHomeUtils;
import nextcp.upnp.modelGen.avopenhomeorg.product1.ProductService;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetStandbyInput;

/**
 * Actions
 */
public class OhProductServiceBridge implements IProductService
{
    private static final Logger log = LoggerFactory.getLogger(OhProductServiceBridge.class.getName());
    
    private ProductService productService = null;
    private OpenHomeUtils ohUtil = null;
    private OhProductServiceEventListener listener = null;

    private LinkedList<InputSourceDto> sourceList = null;

    public OhProductServiceBridge(ProductService productService, OhProductServiceEventListener listener, DtoBuilder dtoBuilder)
    {
        this.productService = productService;
        this.ohUtil = new OpenHomeUtils(dtoBuilder);
        this.listener = listener;

        String xml = productService.sourceXml().Value;
        log.info("Input Sources : " + xml);
        sourceList = ohUtil.convertToInputSourceList(xml);
        listener.getStateVariable().SourceIndex = productService.sourceIndex().Value;
        log.info("current input source index : " + productService.sourceIndex().Value);
    }

    @Override
    public LinkedList<InputSourceDto> getSourceList()
    {
        return sourceList;
    }

    @Override
    public boolean getStandby()
    {
        return productService.standby().Value;
    }

    @Override
    public void setStandby(boolean goToStandby)
    {
        SetStandbyInput inp = new SetStandbyInput();
        inp.Value = goToStandby;
        productService.setStandby(inp);
    }

    @Override
    public InputSourceDto getInputSource(Long value)
    {
        if (value != null)
        {
            return sourceList.get(Math.toIntExact(value));
        }
        else
        {
            return null;
        }
    }

    @Override
    public InputSourceDto getCurrentInputSource()
    {
        return getInputSource(listener.getStateVariable().SourceIndex);
    }
}
