package nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class GetRadioFilterValues extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRadioFilterValues.class.getName());
    private ActionInvocation<?> invocation;

    public GetRadioFilterValues(Service service, GetRadioFilterValuesInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRadioFilterValues"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Kind", UpnpValue.forInput(getActionInvocation(), "Kind", input.Kind));
        getActionInvocation().setInput("Search", UpnpValue.forInput(getActionInvocation(), "Search", input.Search));
    }

    public GetRadioFilterValuesOutput executeAction()
    {
        invocation = execute();

        GetRadioFilterValuesOutput result = new GetRadioFilterValuesOutput();

        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
