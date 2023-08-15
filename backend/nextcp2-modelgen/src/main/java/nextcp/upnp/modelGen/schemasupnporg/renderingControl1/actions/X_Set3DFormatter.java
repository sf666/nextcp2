package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

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
public class X_Set3DFormatter extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_Set3DFormatter.class.getName());
    private ActionInvocation<?> invocation;

    public X_Set3DFormatter(Service service, X_Set3DFormatterInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_Set3DFormatter"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Desired3DFormatter", input.Desired3DFormatter);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
