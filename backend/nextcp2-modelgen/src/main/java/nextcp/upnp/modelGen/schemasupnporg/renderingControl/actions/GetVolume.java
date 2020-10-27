package nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions;

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
public class GetVolume extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetVolume.class.getName());
    private ActionInvocation<?> invocation;

    public GetVolume(Service service, GetVolumeInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetVolume")), cp);

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
