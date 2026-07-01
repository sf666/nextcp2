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
public class SelectPreset extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SelectPreset.class.getName());
    private ActionInvocation<?> invocation;

    public SelectPreset(Service service, SelectPresetInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SelectPreset"), new NextcpClientInfo()), cp);
		
        if (input.InstanceID != null) {
    	    getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
		} else {
    	    getActionInvocation().setInput("InstanceID", null);
		}
        if (input.PresetName != null) {
	        getActionInvocation().setInput("PresetName", input.PresetName);
		} else {
    	    getActionInvocation().setInput("PresetName", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
