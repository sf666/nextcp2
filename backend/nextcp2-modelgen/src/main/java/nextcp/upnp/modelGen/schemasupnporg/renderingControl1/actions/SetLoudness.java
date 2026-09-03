package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

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
public class SetLoudness extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetLoudness.class.getName());
    private ActionInvocation<?> invocation;

    public SetLoudness(Service service, SetLoudnessInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetLoudness"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Channel", UpnpValue.forInput(getActionInvocation(), "Channel", input.Channel));
        getActionInvocation().setInput("DesiredLoudness", UpnpValue.forInput(getActionInvocation(), "DesiredLoudness", input.DesiredLoudness));
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
