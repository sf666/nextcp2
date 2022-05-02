package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class SetTidalConnectEnable extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetTidalConnectEnable.class.getName());
    private ActionInvocation<?> invocation;

    public SetTidalConnectEnable(Service service, SetTidalConnectEnableInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetTidalConnectEnable"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Value", input.Value);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}