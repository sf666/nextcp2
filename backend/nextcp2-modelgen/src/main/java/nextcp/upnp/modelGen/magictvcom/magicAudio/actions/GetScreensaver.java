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
public class GetScreensaver extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetScreensaver.class.getName());
    private ActionInvocation<?> invocation;

    public GetScreensaver(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetScreensaver"), new NextcpClientInfo()), cp);

    }

    public GetScreensaverOutput executeAction()
    {
        invocation = execute();

        GetScreensaverOutput result = new GetScreensaverOutput();

        result.Mode = ((UnsignedIntegerFourBytes) invocation.getOutput("Mode").getValue()).getValue();
        result.Timeout = ((UnsignedIntegerFourBytes) invocation.getOutput("Timeout").getValue()).getValue();

        return result;
    }
}
