package nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions;

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
public class SetToken extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetToken.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public SetToken(Service service, SetTokenInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetToken"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ServiceId", input.ServiceId);
        getActionInvocation().setInput("TokenId", input.TokenId);
        getActionInvocation().setInput("AesKeyRsaEncrypted", b64.getString(input.AesKeyRsaEncrypted));
        getActionInvocation().setInput("InitVectorRsaEncrypted", b64.getString(input.InitVectorRsaEncrypted));
        getActionInvocation().setInput("TokenAesEncrypted", b64.getString(input.TokenAesEncrypted));
        getActionInvocation().setInput("IsLongLived", input.IsLongLived);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
