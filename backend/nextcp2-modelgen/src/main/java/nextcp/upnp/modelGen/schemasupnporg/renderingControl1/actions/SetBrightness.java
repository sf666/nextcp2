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
public class SetBrightness extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetBrightness.class.getName());
    private ActionInvocation<?> invocation;

    public SetBrightness(Service service, SetBrightnessInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetBrightness"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("DesiredBrightness", UpnpValue.forInput(getActionInvocation(), "DesiredBrightness", input.DesiredBrightness));
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
