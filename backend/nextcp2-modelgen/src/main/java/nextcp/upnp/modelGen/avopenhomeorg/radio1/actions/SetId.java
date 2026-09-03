package nextcp.upnp.modelGen.avopenhomeorg.radio1.actions;

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
public class SetId extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetId.class.getName());
    private ActionInvocation<?> invocation;

    public SetId(Service service, SetIdInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetId"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Uri", UpnpValue.forInput(getActionInvocation(), "Uri", input.Uri));
        getActionInvocation().setInput("Value", UpnpValue.forInput(getActionInvocation(), "Value", input.Value));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
