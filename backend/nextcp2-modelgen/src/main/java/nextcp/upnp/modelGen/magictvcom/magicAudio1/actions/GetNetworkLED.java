package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class GetNetworkLED extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetNetworkLED.class.getName());
    private ActionInvocation<?> invocation;

    public GetNetworkLED(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetNetworkLED"), new NextcpClientInfo()), cp);

    }

    public GetNetworkLEDOutput executeAction()
    {
        invocation = execute();

        GetNetworkLEDOutput result = new GetNetworkLEDOutput();

        BooleanDatatype data_Value = new BooleanDatatype();
        result.Value = data_Value.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
