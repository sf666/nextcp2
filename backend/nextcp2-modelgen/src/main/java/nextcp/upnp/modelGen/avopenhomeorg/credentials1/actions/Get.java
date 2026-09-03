package nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions;

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
public class Get extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Get.class.getName());
    private ActionInvocation<?> invocation;

    public Get(Service service, GetInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Get"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Id", UpnpValue.forInput(getActionInvocation(), "Id", input.Id));
    }

    public GetOutput executeAction()
    {
        invocation = execute();

        GetOutput result = new GetOutput();

        result.Data = UpnpValue.toTextOrEmpty(invocation.getOutput("Data").getValue());
        result.Enabled = UpnpValue.toBoolean(invocation.getOutput("Enabled").getValue());
        result.Password = UpnpValue.toBytes(invocation.getOutput("Password").getValue());
        result.Status = UpnpValue.toTextOrEmpty(invocation.getOutput("Status").getValue());
        result.UserName = UpnpValue.toTextOrEmpty(invocation.getOutput("UserName").getValue());

        return result;
    }
}
