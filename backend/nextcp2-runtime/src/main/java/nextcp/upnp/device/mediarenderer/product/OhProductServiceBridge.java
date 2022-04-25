package nextcp.upnp.device.mediarenderer.product;

import java.util.LinkedList;

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
        sourceList = ohUtil.convertToInputSourceList(xml);
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
        return sourceList.get(Math.toIntExact(value));
    }

    @Override
    public InputSourceDto getCurrentInputSource()
    {
        return getInputSource(listener.getStateVariable().SourceIndex);
    }
}
