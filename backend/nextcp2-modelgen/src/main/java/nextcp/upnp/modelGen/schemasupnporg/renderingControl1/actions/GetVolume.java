package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

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
public class GetVolume extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetVolume.class.getName());
    private ActionInvocation<?> invocation;

    public GetVolume(Service service, GetVolumeInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetVolume"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Channel", input.Channel);
    }

    public GetVolumeOutput executeAction()
    {
        invocation = execute();

        GetVolumeOutput result = new GetVolumeOutput();

        result.CurrentVolume = ((UnsignedIntegerFourBytes) invocation.getOutput("CurrentVolume").getValue()).getValue();

        return result;
    }
}
