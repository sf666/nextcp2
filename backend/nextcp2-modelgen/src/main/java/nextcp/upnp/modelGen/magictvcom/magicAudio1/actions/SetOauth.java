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
import nextcp.upnp.UpnpValue;

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

    public SetOauth(Service service, SetOauthInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetOauth"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("AccessExpiry", UpnpValue.forInput(getActionInvocation(), "AccessExpiry", input.AccessExpiry));
        getActionInvocation().setInput("AccessToken", UpnpValue.forInput(getActionInvocation(), "AccessToken", input.AccessToken));
        getActionInvocation().setInput("ClientId", UpnpValue.forInput(getActionInvocation(), "ClientId", input.ClientId));
        getActionInvocation().setInput("ClientSecret", UpnpValue.forInput(getActionInvocation(), "ClientSecret", input.ClientSecret));
        getActionInvocation().setInput("RefreshToken", UpnpValue.forInput(getActionInvocation(), "RefreshToken", input.RefreshToken));
        getActionInvocation().setInput("ServiceId", UpnpValue.forInput(getActionInvocation(), "ServiceId", input.ServiceId));
        getActionInvocation().setInput("UserName", UpnpValue.forInput(getActionInvocation(), "UserName", input.UserName));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
