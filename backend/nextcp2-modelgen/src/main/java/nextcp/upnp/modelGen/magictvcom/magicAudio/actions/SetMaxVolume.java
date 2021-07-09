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
public class SetMaxVolume extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetMaxVolume.class.getName());
    private ActionInvocation<?> invocation;

    public SetMaxVolume(Service service, SetMaxVolumeInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetMaxVolume"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Value", new UnsignedIntegerFourBytes(input.Value));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
