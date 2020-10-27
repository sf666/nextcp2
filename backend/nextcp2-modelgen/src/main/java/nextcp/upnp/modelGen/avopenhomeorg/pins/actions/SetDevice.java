package nextcp.upnp.modelGen.avopenhomeorg.pins.actions;

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
public class SetDevice extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetDevice.class.getName());
    private ActionInvocation<?> invocation;

    public SetDevice(Service service, SetDeviceInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetDevice")), cp);

        getActionInvocation().setInput("Index", new UnsignedIntegerFourBytes(input.Index));
        getActionInvocation().setInput("Mode", input.Mode);
        getActionInvocation().setInput("Type", input.Type);
        getActionInvocation().setInput("Uri", input.Uri);
        getActionInvocation().setInput("Title", input.Title);
        getActionInvocation().setInput("Description", input.Description);
        getActionInvocation().setInput("ArtworkUri", input.ArtworkUri);
        getActionInvocation().setInput("Shuffle", input.Shuffle);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
