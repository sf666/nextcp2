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
public class SetChannel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetChannel.class.getName());
    private ActionInvocation<?> invocation;

    public SetChannel(Service service, SetChannelInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetChannel"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Metadata", UpnpValue.forInput(getActionInvocation(), "Metadata", input.Metadata));
        getActionInvocation().setInput("Uri", UpnpValue.forInput(getActionInvocation(), "Uri", input.Uri));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
