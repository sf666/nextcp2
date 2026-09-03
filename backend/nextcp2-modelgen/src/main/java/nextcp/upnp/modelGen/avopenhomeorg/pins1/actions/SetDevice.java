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
import nextcp.upnp.UpnpValue;

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
		
        getActionInvocation().setInput("ArtworkUri", UpnpValue.forInput(getActionInvocation(), "ArtworkUri", input.ArtworkUri));
        getActionInvocation().setInput("Description", UpnpValue.forInput(getActionInvocation(), "Description", input.Description));
        getActionInvocation().setInput("Index", UpnpValue.forInput(getActionInvocation(), "Index", input.Index));
        getActionInvocation().setInput("Mode", UpnpValue.forInput(getActionInvocation(), "Mode", input.Mode));
        getActionInvocation().setInput("Shuffle", UpnpValue.forInput(getActionInvocation(), "Shuffle", input.Shuffle));
        getActionInvocation().setInput("Title", UpnpValue.forInput(getActionInvocation(), "Title", input.Title));
        getActionInvocation().setInput("Type", UpnpValue.forInput(getActionInvocation(), "Type", input.Type));
        getActionInvocation().setInput("Uri", UpnpValue.forInput(getActionInvocation(), "Uri", input.Uri));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
