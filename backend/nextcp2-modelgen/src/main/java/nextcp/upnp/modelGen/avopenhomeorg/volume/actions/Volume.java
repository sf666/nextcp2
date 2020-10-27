package nextcp.upnp.modelGen.avopenhomeorg.volume.actions;

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
public class Volume extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Volume.class.getName());
    private ActionInvocation<?> invocation;

    public Volume(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Volume")), cp);

    }

    public VolumeOutput executeAction()
    {
        invocation = execute();

        VolumeOutput result = new VolumeOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
