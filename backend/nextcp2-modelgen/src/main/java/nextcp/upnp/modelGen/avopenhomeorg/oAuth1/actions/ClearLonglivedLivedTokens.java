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

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class ClearLonglivedLivedTokens extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ClearLonglivedLivedTokens.class.getName());
    private ActionInvocation<?> invocation;

    public ClearLonglivedLivedTokens(Service service, ClearLonglivedLivedTokensInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ClearLonglivedLivedTokens"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ServiceId", input.ServiceId);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
