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
public class GetNetworkLEDControl extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetNetworkLEDControl.class.getName());
    private ActionInvocation<?> invocation;

    public GetNetworkLEDControl(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetNetworkLEDControl"), new NextcpClientInfo()), cp);

    }

    public GetNetworkLEDControlOutput executeAction()
    {
        invocation = execute();

        GetNetworkLEDControlOutput result = new GetNetworkLEDControlOutput();

        BooleanDatatype data_Value = new BooleanDatatype();
        result.Value = data_Value.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
