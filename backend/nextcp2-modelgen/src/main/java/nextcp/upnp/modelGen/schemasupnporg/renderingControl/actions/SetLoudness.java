package nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions;

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
public class SetLoudness extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetLoudness.class.getName());
    private ActionInvocation<?> invocation;

    public SetLoudness(Service service, SetLoudnessInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetLoudness"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Channel", input.Channel);
        getActionInvocation().setInput("DesiredLoudness", input.DesiredLoudness);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
