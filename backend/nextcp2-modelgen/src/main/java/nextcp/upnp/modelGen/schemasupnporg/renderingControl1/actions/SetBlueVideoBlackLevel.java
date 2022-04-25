package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

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
public class SetBlueVideoBlackLevel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetBlueVideoBlackLevel.class.getName());
    private ActionInvocation<?> invocation;

    public SetBlueVideoBlackLevel(Service service, SetBlueVideoBlackLevelInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetBlueVideoBlackLevel"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("DesiredBlueVideoBlackLevel", new UnsignedIntegerTwoBytes(input.DesiredBlueVideoBlackLevel));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
