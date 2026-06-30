package nextcp.upnp.device.mediarenderer.product;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.services.IProductService;
import nextcp.dto.InputSourceDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.OpenHomeUtils;
import nextcp.upnp.modelGen.avopenhomeorg.product1.ProductService;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndexInput;
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

        try
        {
            String xml = productService.sourceXml().Value;
            log.info("Input Sources : " + xml);
            sourceList = ohUtil.convertToInputSourceList(xml);
            listener.getStateVariable().SourceIndex = productService.sourceIndex().Value;
            log.info("current input source index : " + productService.sourceIndex().Value);
        }
        catch (Exception e)
        {
            log.info("SourceXML not implemented ...");
        }
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
        if (value != null && sourceList != null)
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

    @Override
    public void switchToSource(String type)
    {
        if (sourceList == null)
        {
            log.warn("cannot switch to source '{}' : source list unavailable", type);
            return;
        }
        for (int index = 0; index < sourceList.size(); index++)
        {
            InputSourceDto src = sourceList.get(index);
            if (type.equalsIgnoreCase(src.Type))
            {
                log.info("switchToSource: target index={} name='{}' type='{}' ; current source index before switch = {}",
                    index, src.Name, src.Type, readCurrentSourceIndex());
                SetSourceIndexInput inp = new SetSourceIndexInput();
                inp.Value = (long) index;
                productService.setSourceIndex(inp);
                Long after = readCurrentSourceIndex();
                log.info("switchToSource: setSourceIndex({}) sent ; current source index after switch = {} (expected {})",
                    index, after, index);
                return;
            }
        }
        log.warn("renderer has no source of type '{}'", type);
    }

    private Long readCurrentSourceIndex()
    {
        try
        {
            return productService.sourceIndex().Value;
        }
        catch (Exception e)
        {
            log.debug("could not read current source index: {}", e.getMessage());
            return null;
        }
    }
}
