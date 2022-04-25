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
public class SetTuneInDetails extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetTuneInDetails.class.getName());
    private ActionInvocation<?> invocation;

    public SetTuneInDetails(Service service, SetTuneInDetailsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetTuneInDetails"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("UserName", input.UserName);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
