package nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions;

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
public class X_GetStoppedReason extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_GetStoppedReason.class.getName());
    private ActionInvocation<?> invocation;

    public X_GetStoppedReason(Service service, X_GetStoppedReasonInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_GetStoppedReason"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public X_GetStoppedReasonOutput executeAction()
    {
        invocation = execute();

        X_GetStoppedReasonOutput result = new X_GetStoppedReasonOutput();

        result.StoppedReason = UpnpValue.toTextOrEmpty(invocation.getOutput("StoppedReason").getValue());
        result.StoppedReasonData = UpnpValue.toTextOrEmpty(invocation.getOutput("StoppedReasonData").getValue());

        return result;
    }
}
