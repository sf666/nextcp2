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
public class GetVerticalKeystone extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetVerticalKeystone.class.getName());
    private ActionInvocation<?> invocation;

    public GetVerticalKeystone(Service service, GetVerticalKeystoneInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetVerticalKeystone"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetVerticalKeystoneOutput executeAction()
    {
        invocation = execute();

        GetVerticalKeystoneOutput result = new GetVerticalKeystoneOutput();

        result.CurrentVerticalKeystone = Integer.valueOf(invocation.getOutput("CurrentVerticalKeystone").getValue().toString());

        return result;
    }
}
