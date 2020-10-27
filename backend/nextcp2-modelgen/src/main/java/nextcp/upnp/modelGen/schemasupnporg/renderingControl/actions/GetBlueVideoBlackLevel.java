package nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions;

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
public class GetBlueVideoBlackLevel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetBlueVideoBlackLevel.class.getName());
    private ActionInvocation<?> invocation;

    public GetBlueVideoBlackLevel(Service service, GetBlueVideoBlackLevelInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetBlueVideoBlackLevel")), cp);

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
