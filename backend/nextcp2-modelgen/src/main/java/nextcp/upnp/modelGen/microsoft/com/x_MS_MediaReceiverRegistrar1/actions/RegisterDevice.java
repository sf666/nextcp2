package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class RegisterDevice extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(RegisterDevice.class.getName());
    private ActionInvocation<?> invocation;

    public RegisterDevice(Service service, RegisterDeviceInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("RegisterDevice"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("RegistrationReqMsg", UpnpValue.forInput(getActionInvocation(), "RegistrationReqMsg", input.RegistrationReqMsg));
    }

    public RegisterDeviceOutput executeAction()
    {
        invocation = execute();

        RegisterDeviceOutput result = new RegisterDeviceOutput();

        result.RegistrationRespMsg = UpnpValue.toBytes(invocation.getOutput("RegistrationRespMsg").getValue());

        return result;
    }
}
