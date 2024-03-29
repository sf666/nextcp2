package nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class GetUpdateId extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetUpdateId.class.getName());
    private ActionInvocation<?> invocation;

    public GetUpdateId(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetUpdateId"), new NextcpClientInfo()), cp);

    }

    public GetUpdateIdOutput executeAction()
    {
        invocation = execute();

        GetUpdateIdOutput result = new GetUpdateIdOutput();

        result.UpdateId = ((UnsignedIntegerFourBytes) invocation.getOutput("UpdateId").getValue()).getValue();

        return result;
    }
}
