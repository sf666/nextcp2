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
public class SetGreenVideoGain extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetGreenVideoGain.class.getName());
    private ActionInvocation<?> invocation;

    public SetGreenVideoGain(Service service, SetGreenVideoGainInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetGreenVideoGain")), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("DesiredGreenVideoGain", new UnsignedIntegerFourBytes(input.DesiredGreenVideoGain));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
