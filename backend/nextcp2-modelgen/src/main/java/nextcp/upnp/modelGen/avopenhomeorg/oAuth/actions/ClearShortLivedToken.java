package nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions;

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
 */
public class ClearShortLivedToken extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ClearShortLivedToken.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public ClearShortLivedToken(Service service, ClearShortLivedTokenInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ClearShortLivedToken"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ServiceId", input.ServiceId);
        getActionInvocation().setInput("TokenId", input.TokenId);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
