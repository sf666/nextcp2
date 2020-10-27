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
public class GetColorTemperature extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetColorTemperature.class.getName());
    private ActionInvocation<?> invocation;

    public GetColorTemperature(Service service, GetColorTemperatureInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetColorTemperature")), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetColorTemperatureOutput executeAction()
    {
        invocation = execute();

        GetColorTemperatureOutput result = new GetColorTemperatureOutput();

        result.CurrentColorTemperature = ((UnsignedIntegerFourBytes) invocation.getOutput("CurrentColorTemperature").getValue()).getValue();

        return result;
    }
}
