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
public class GetGreenVideoBlackLevel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetGreenVideoBlackLevel.class.getName());
    private ActionInvocation<?> invocation;

    public GetGreenVideoBlackLevel(Service service, GetGreenVideoBlackLevelInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetGreenVideoBlackLevel"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetGreenVideoBlackLevelOutput executeAction()
    {
        invocation = execute();

        GetGreenVideoBlackLevelOutput result = new GetGreenVideoBlackLevelOutput();

        result.CurrentGreenVideoBlackLevel = ((UnsignedIntegerTwoBytes) invocation.getOutput("CurrentGreenVideoBlackLevel").getValue()).getValue();

        return result;
    }
}
