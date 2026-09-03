package nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions;

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
public class GetIds extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetIds.class.getName());
    private ActionInvocation<?> invocation;

    public GetIds(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetIds"), new NextcpClientInfo()), cp);
		
    }

    public GetIdsOutput executeAction()
    {
        invocation = execute();

        GetIdsOutput result = new GetIdsOutput();

        result.Ids = UpnpValue.toTextOrEmpty(invocation.getOutput("Ids").getValue());

        return result;
    }
}
