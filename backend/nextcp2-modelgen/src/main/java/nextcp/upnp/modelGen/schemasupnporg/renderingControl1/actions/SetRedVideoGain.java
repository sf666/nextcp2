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
public class SetRedVideoGain extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetRedVideoGain.class.getName());
    private ActionInvocation<?> invocation;

    public SetRedVideoGain(Service service, SetRedVideoGainInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetRedVideoGain"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("DesiredRedVideoGain", UpnpValue.forInput(getActionInvocation(), "DesiredRedVideoGain", input.DesiredRedVideoGain));
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
