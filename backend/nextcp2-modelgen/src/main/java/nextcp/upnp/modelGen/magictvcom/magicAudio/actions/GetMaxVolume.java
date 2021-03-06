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
public class GetMaxVolume extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMaxVolume.class.getName());
    private ActionInvocation<?> invocation;

    public GetMaxVolume(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMaxVolume"), new NextcpClientInfo()), cp);

    }

    public GetMaxVolumeOutput executeAction()
    {
        invocation = execute();

        GetMaxVolumeOutput result = new GetMaxVolumeOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
