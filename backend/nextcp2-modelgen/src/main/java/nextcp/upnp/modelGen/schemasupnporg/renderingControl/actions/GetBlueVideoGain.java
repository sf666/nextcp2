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
public class GetBlueVideoGain extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetBlueVideoGain.class.getName());
    private ActionInvocation<?> invocation;

    public GetBlueVideoGain(Service service, GetBlueVideoGainInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetBlueVideoGain"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetBlueVideoGainOutput executeAction()
    {
        invocation = execute();

        GetBlueVideoGainOutput result = new GetBlueVideoGainOutput();

        result.CurrentBlueVideoGain = ((UnsignedIntegerFourBytes) invocation.getOutput("CurrentBlueVideoGain").getValue()).getValue();

        return result;
    }
}
