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

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class SetMute extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetMute.class.getName());
    private ActionInvocation<?> invocation;

    public SetMute(Service service, SetMuteInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetMute"), new NextcpClientInfo()), cp);
		
        if (input.InstanceID != null) {
    	    getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
		} else {
    	    getActionInvocation().setInput("InstanceID", null);
		}
        if (input.Channel != null) {
	        getActionInvocation().setInput("Channel", input.Channel);
		} else {
    	    getActionInvocation().setInput("Channel", null);
		}
        if (input.DesiredMute != null) {
        	getActionInvocation().setInput("DesiredMute", input.DesiredMute);
		} else {
    	    getActionInvocation().setInput("DesiredMute", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
