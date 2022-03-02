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
 *
 * Template: action.ftl
 *  
 */
public class GetRedVideoBlackLevel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRedVideoBlackLevel.class.getName());
    private ActionInvocation<?> invocation;

    public GetRedVideoBlackLevel(Service service, GetRedVideoBlackLevelInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRedVideoBlackLevel"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetRedVideoBlackLevelOutput executeAction()
    {
        invocation = execute();

        GetRedVideoBlackLevelOutput result = new GetRedVideoBlackLevelOutput();

        result.CurrentRedVideoBlackLevel = ((UnsignedIntegerTwoBytes) invocation.getOutput("CurrentRedVideoBlackLevel").getValue()).getValue();

        return result;
    }
}
