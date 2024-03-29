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
public class GetHorizontalKeystone extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetHorizontalKeystone.class.getName());
    private ActionInvocation<?> invocation;

    public GetHorizontalKeystone(Service service, GetHorizontalKeystoneInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetHorizontalKeystone"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetHorizontalKeystoneOutput executeAction()
    {
        invocation = execute();

        GetHorizontalKeystoneOutput result = new GetHorizontalKeystoneOutput();

        result.CurrentHorizontalKeystone = Integer.valueOf(invocation.getOutput("CurrentHorizontalKeystone").getValue().toString());

        return result;
    }
}
