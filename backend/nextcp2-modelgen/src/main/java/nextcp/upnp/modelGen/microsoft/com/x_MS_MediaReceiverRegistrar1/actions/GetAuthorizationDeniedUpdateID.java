package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions;

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
public class GetAuthorizationDeniedUpdateID extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAuthorizationDeniedUpdateID.class.getName());
    private ActionInvocation<?> invocation;

    public GetAuthorizationDeniedUpdateID(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAuthorizationDeniedUpdateID"), new NextcpClientInfo()), cp);

    }

    public GetAuthorizationDeniedUpdateIDOutput executeAction()
    {
        invocation = execute();

        GetAuthorizationDeniedUpdateIDOutput result = new GetAuthorizationDeniedUpdateIDOutput();

        result.AuthorizationDeniedUpdateID = ((UnsignedIntegerFourBytes) invocation.getOutput("AuthorizationDeniedUpdateID").getValue()).getValue();

        return result;
    }
}
