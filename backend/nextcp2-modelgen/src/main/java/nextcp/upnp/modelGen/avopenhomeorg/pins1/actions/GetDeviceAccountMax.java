package nextcp.upnp.modelGen.avopenhomeorg.pins1.actions;

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
public class GetDeviceAccountMax extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetDeviceAccountMax.class.getName());
    private ActionInvocation<?> invocation;

    public GetDeviceAccountMax(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetDeviceAccountMax"), new NextcpClientInfo()), cp);

    }

    public GetDeviceAccountMaxOutput executeAction()
    {
        invocation = execute();

        GetDeviceAccountMaxOutput result = new GetDeviceAccountMaxOutput();

        result.DeviceMax = ((UnsignedIntegerFourBytes) invocation.getOutput("DeviceMax").getValue()).getValue();
        result.AccountMax = ((UnsignedIntegerFourBytes) invocation.getOutput("AccountMax").getValue()).getValue();

        return result;
    }
}
