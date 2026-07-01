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
public class SetVolume extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetVolume.class.getName());
    private ActionInvocation<?> invocation;

    public SetVolume(Service service, SetVolumeInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetVolume"), new NextcpClientInfo()), cp);
		
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
        if (input.DesiredVolume != null) {
        	getActionInvocation().setInput("DesiredVolume", new UnsignedIntegerTwoBytes(input.DesiredVolume));
		} else {
    	    getActionInvocation().setInput("DesiredVolume", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
