package nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions;

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
public class SetNetwork extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetNetwork.class.getName());
    private ActionInvocation<?> invocation;

    public SetNetwork(Service service, SetNetworkInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetNetwork"), new NextcpClientInfo()), cp);
		
        if (input.SSID != null) {
	        getActionInvocation().setInput("SSID", input.SSID);
		} else {
    	    getActionInvocation().setInput("SSID", null);
		}
        if (input.Key != null) {
	        getActionInvocation().setInput("Key", input.Key);
		} else {
    	    getActionInvocation().setInput("Key", null);
		}
        if (input.AuthAlgo != null) {
	        getActionInvocation().setInput("AuthAlgo", input.AuthAlgo);
		} else {
    	    getActionInvocation().setInput("AuthAlgo", null);
		}
        if (input.CipherAlgo != null) {
	        getActionInvocation().setInput("CipherAlgo", input.CipherAlgo);
		} else {
    	    getActionInvocation().setInput("CipherAlgo", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
