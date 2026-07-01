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
public class SetOauth extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetOauth.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public SetOauth(Service service, SetOauthInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetOauth"), new NextcpClientInfo()), cp);
		
        if (input.ServiceId != null) {
	        getActionInvocation().setInput("ServiceId", input.ServiceId);
		} else {
    	    getActionInvocation().setInput("ServiceId", null);
		}
        if (input.ClientId != null) {
    	    getActionInvocation().setInput("ClientId", b64.getString(input.ClientId));
		} else {
    	    getActionInvocation().setInput("ClientId", null);
		}
        if (input.ClientSecret != null) {
    	    getActionInvocation().setInput("ClientSecret", b64.getString(input.ClientSecret));
		} else {
    	    getActionInvocation().setInput("ClientSecret", null);
		}
        if (input.AccessToken != null) {
    	    getActionInvocation().setInput("AccessToken", b64.getString(input.AccessToken));
		} else {
    	    getActionInvocation().setInput("AccessToken", null);
		}
        if (input.RefreshToken != null) {
    	    getActionInvocation().setInput("RefreshToken", b64.getString(input.RefreshToken));
		} else {
    	    getActionInvocation().setInput("RefreshToken", null);
		}
        if (input.AccessExpiry != null) {
    	    getActionInvocation().setInput("AccessExpiry", new UnsignedIntegerFourBytes(input.AccessExpiry));
		} else {
    	    getActionInvocation().setInput("AccessExpiry", null);
		}
        if (input.UserName != null) {
	        getActionInvocation().setInput("UserName", input.UserName);
		} else {
    	    getActionInvocation().setInput("UserName", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
