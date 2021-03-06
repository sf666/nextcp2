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
public class GetGreenVideoGain extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetGreenVideoGain.class.getName());
    private ActionInvocation<?> invocation;

    public GetGreenVideoGain(Service service, GetGreenVideoGainInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetGreenVideoGain"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetGreenVideoGainOutput executeAction()
    {
        invocation = execute();

        GetGreenVideoGainOutput result = new GetGreenVideoGainOutput();

        result.CurrentGreenVideoGain = ((UnsignedIntegerTwoBytes) invocation.getOutput("CurrentGreenVideoGain").getValue()).getValue();

        return result;
    }
}
