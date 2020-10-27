package nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class X_SetBookmark extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_SetBookmark.class.getName());
    private ActionInvocation<?> invocation;

    public X_SetBookmark(Service service, X_SetBookmarkInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_SetBookmark")), cp);

        getActionInvocation().setInput("CategoryType", new UnsignedIntegerFourBytes(input.CategoryType));
        getActionInvocation().setInput("RID", new UnsignedIntegerFourBytes(input.RID));
        getActionInvocation().setInput("ObjectID", input.ObjectID);
        getActionInvocation().setInput("PosSecond", new UnsignedIntegerFourBytes(input.PosSecond));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
