package nextcp.upnp.modelGen.magictvcom.magicAudio.actions;

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
public class SetAutoPlay extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetAutoPlay.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public SetAutoPlay(Service service, SetAutoPlayInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetAutoPlay"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Value", input.Value);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
