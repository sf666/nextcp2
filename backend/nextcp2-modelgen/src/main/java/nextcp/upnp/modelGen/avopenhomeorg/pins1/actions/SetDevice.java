package nextcp.upnp.modelGen.avopenhomeorg.pins1.actions;

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
public class SetDevice extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetDevice.class.getName());
    private ActionInvocation<?> invocation;

    public SetDevice(Service service, SetDeviceInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetDevice"), new NextcpClientInfo()), cp);

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
