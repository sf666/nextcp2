package nextcp.upnp.modelGen.jminimorg.log2.actions;

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
public class SetLogStart extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetLogStart.class.getName());
    private ActionInvocation<?> invocation;

    public SetLogStart(Service service, SetLogStartInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetLogStart"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("StartPosition", new UnsignedIntegerFourBytes(input.StartPosition));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
