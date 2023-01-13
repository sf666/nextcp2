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
public class SetHorizontalKeystone extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetHorizontalKeystone.class.getName());
    private ActionInvocation<?> invocation;

    public SetHorizontalKeystone(Service service, SetHorizontalKeystoneInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetHorizontalKeystone"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("DesiredHorizontalKeystone", new IntegerDatatype(input.DesiredHorizontalKeystone));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
