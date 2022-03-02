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
public class ClearShortLivedTokens extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ClearShortLivedTokens.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public ClearShortLivedTokens(Service service, ClearShortLivedTokensInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ClearShortLivedTokens"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ServiceId", input.ServiceId);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
