package nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions;

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
public class X_LG_Seek extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_LG_Seek.class.getName());
    private ActionInvocation<?> invocation;

    public X_LG_Seek(Service service, X_LG_SeekInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_LG_Seek"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Unit", input.Unit);
        getActionInvocation().setInput("Target", input.Target);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
