package nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions;

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
public class SetToken extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetToken.class.getName());
    private ActionInvocation<?> invocation;

    public SetToken(Service service, SetTokenInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetToken"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("AesKeyRsaEncrypted", UpnpValue.forInput(getActionInvocation(), "AesKeyRsaEncrypted", input.AesKeyRsaEncrypted));
        getActionInvocation().setInput("InitVectorRsaEncrypted", UpnpValue.forInput(getActionInvocation(), "InitVectorRsaEncrypted", input.InitVectorRsaEncrypted));
        getActionInvocation().setInput("IsLongLived", UpnpValue.forInput(getActionInvocation(), "IsLongLived", input.IsLongLived));
        getActionInvocation().setInput("ServiceId", UpnpValue.forInput(getActionInvocation(), "ServiceId", input.ServiceId));
        getActionInvocation().setInput("TokenAesEncrypted", UpnpValue.forInput(getActionInvocation(), "TokenAesEncrypted", input.TokenAesEncrypted));
        getActionInvocation().setInput("TokenId", UpnpValue.forInput(getActionInvocation(), "TokenId", input.TokenId));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
