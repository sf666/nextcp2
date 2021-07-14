package nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions;

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
public class X_SetTVSlideShow extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_SetTVSlideShow.class.getName());
    private ActionInvocation<?> invocation;

    public X_SetTVSlideShow(Service service, X_SetTVSlideShowInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_SetTVSlideShow"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("CurrentShowState", input.CurrentShowState);
        getActionInvocation().setInput("CurrentShowTheme", new UnsignedIntegerFourBytes(input.CurrentShowTheme));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
