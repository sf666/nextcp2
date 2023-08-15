package nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions;

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
public class X_SetBookmark extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_SetBookmark.class.getName());
    private ActionInvocation<?> invocation;

    public X_SetBookmark(Service service, X_SetBookmarkInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_SetBookmark"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ObjectID", input.ObjectID);
        getActionInvocation().setInput("PosSecond", new UnsignedIntegerFourBytes(input.PosSecond));
        getActionInvocation().setInput("CategoryType", input.CategoryType);
        getActionInvocation().setInput("RID", input.RID);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
