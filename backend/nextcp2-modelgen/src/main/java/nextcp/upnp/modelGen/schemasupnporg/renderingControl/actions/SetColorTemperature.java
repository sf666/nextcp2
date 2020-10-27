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
public class SetColorTemperature extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetColorTemperature.class.getName());
    private ActionInvocation<?> invocation;

    public SetColorTemperature(Service service, SetColorTemperatureInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetColorTemperature")), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("DesiredColorTemperature", new UnsignedIntegerFourBytes(input.DesiredColorTemperature));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
