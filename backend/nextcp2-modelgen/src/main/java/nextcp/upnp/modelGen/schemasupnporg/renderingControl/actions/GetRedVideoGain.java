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
public class GetRedVideoGain extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRedVideoGain.class.getName());
    private ActionInvocation<?> invocation;

    public GetRedVideoGain(Service service, GetRedVideoGainInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRedVideoGain")), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetRedVideoGainOutput executeAction()
    {
        invocation = execute();

        GetRedVideoGainOutput result = new GetRedVideoGainOutput();

        result.CurrentRedVideoGain = ((UnsignedIntegerFourBytes) invocation.getOutput("CurrentRedVideoGain").getValue()).getValue();

        return result;
    }
}
