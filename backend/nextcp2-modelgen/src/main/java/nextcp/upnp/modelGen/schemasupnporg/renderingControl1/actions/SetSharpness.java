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
public class SetSharpness extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetSharpness.class.getName());
    private ActionInvocation<?> invocation;

    public SetSharpness(Service service, SetSharpnessInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetSharpness"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("DesiredSharpness", new UnsignedIntegerFourBytes(input.DesiredSharpness));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
