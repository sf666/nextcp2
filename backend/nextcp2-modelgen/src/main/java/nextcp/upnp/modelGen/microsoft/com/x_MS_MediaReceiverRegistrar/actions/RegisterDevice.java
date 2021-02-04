package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar.actions;

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
public class RegisterDevice extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(RegisterDevice.class.getName());
    private ActionInvocation<?> invocation;

    public RegisterDevice(Service service, RegisterDeviceInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("RegisterDevice"), new NextcpClientInfo()), cp);

        throw new RuntimeException("(Base64Datatype)");
    }

    public RegisterDeviceOutput executeAction()
    {
        invocation = execute();

        RegisterDeviceOutput result = new RegisterDeviceOutput();

        result.RegistrationRespMsg = (byte[]) invocation.getOutput("RegistrationRespMsg").getValue();

        return result;
    }
}
