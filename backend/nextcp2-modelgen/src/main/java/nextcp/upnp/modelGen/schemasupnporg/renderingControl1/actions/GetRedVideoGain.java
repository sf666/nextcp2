package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class GetRedVideoGain extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRedVideoGain.class.getName());
    private ActionInvocation<?> invocation;

    public GetRedVideoGain(Service service, GetRedVideoGainInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRedVideoGain"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetRedVideoGainOutput executeAction()
    {
        invocation = execute();

        GetRedVideoGainOutput result = new GetRedVideoGainOutput();

        result.CurrentRedVideoGain = ((UnsignedIntegerTwoBytes) invocation.getOutput("CurrentRedVideoGain").getValue()).getValue();

        return result;
    }
}
