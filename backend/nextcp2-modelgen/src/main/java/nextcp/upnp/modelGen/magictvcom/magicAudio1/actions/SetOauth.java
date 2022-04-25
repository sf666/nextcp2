package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

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

        getActionInvocation().setInput("ServiceId", input.ServiceId);
        getActionInvocation().setInput("ClientId", b64.getString(input.ClientId));
        getActionInvocation().setInput("ClientSecret", b64.getString(input.ClientSecret));
        getActionInvocation().setInput("AccessToken", b64.getString(input.AccessToken));
        getActionInvocation().setInput("RefreshToken", b64.getString(input.RefreshToken));
        getActionInvocation().setInput("AccessExpiry", new UnsignedIntegerFourBytes(input.AccessExpiry));
        getActionInvocation().setInput("UserName", input.UserName);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
