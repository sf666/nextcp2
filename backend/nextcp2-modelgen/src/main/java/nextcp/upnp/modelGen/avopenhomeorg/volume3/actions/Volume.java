package nextcp.upnp.modelGen.avopenhomeorg.volume3.actions;

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
public class Volume extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Volume.class.getName());
    private ActionInvocation<?> invocation;

    public Volume(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Volume"), new NextcpClientInfo()), cp);

    }

    public VolumeOutput executeAction()
    {
        invocation = execute();

        VolumeOutput result = new VolumeOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
