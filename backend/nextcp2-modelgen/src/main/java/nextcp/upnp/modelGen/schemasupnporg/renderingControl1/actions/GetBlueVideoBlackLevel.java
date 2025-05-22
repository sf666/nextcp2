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
public class GetBlueVideoBlackLevel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetBlueVideoBlackLevel.class.getName());
    private ActionInvocation<?> invocation;

    public GetBlueVideoBlackLevel(Service service, GetBlueVideoBlackLevelInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetBlueVideoBlackLevel"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetBlueVideoBlackLevelOutput executeAction()
    {
        invocation = execute();

        GetBlueVideoBlackLevelOutput result = new GetBlueVideoBlackLevelOutput();

        result.CurrentBlueVideoBlackLevel = ((UnsignedIntegerFourBytes) invocation.getOutput("CurrentBlueVideoBlackLevel").getValue()).getValue();

        return result;
    }
}
