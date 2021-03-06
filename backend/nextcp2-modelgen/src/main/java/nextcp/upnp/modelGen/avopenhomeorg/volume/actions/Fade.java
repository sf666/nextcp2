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
public class Fade extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Fade.class.getName());
    private ActionInvocation<?> invocation;

    public Fade(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Fade"), new NextcpClientInfo()), cp);

    }

    public FadeOutput executeAction()
    {
        invocation = execute();

        FadeOutput result = new FadeOutput();

        result.Value = Integer.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
