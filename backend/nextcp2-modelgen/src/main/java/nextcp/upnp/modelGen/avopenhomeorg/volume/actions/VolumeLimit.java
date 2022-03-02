package nextcp.upnp.modelGen.avopenhomeorg.volume.actions;

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
public class VolumeLimit extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(VolumeLimit.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public VolumeLimit(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("VolumeLimit"), new NextcpClientInfo()), cp);

    }

    public VolumeLimitOutput executeAction()
    {
        invocation = execute();

        VolumeLimitOutput result = new VolumeLimitOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
