package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class SetResamplingModeDetails extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetResamplingModeDetails.class.getName());
    private ActionInvocation<?> invocation;

    public SetResamplingModeDetails(Service service, SetResamplingModeDetailsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetResamplingModeDetails"), new NextcpClientInfo()), cp);
		
        if (input.Mode != null) {
	        getActionInvocation().setInput("Mode", input.Mode);
		} else {
    	    getActionInvocation().setInput("Mode", null);
		}
        if (input.FileType != null) {
	        getActionInvocation().setInput("FileType", input.FileType);
		} else {
    	    getActionInvocation().setInput("FileType", null);
		}
        if (input.SrcSamplingRate != null) {
	        getActionInvocation().setInput("SrcSamplingRate", input.SrcSamplingRate);
		} else {
    	    getActionInvocation().setInput("SrcSamplingRate", null);
		}
        if (input.NewSamplingRate != null) {
	        getActionInvocation().setInput("NewSamplingRate", input.NewSamplingRate);
		} else {
    	    getActionInvocation().setInput("NewSamplingRate", null);
		}
        if (input.NewBitDepth != null) {
	        getActionInvocation().setInput("NewBitDepth", input.NewBitDepth);
		} else {
    	    getActionInvocation().setInput("NewBitDepth", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
