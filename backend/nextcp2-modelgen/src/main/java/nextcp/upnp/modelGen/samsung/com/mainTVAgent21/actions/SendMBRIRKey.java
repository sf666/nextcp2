package nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions;

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
public class SendMBRIRKey extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SendMBRIRKey.class.getName());
    private ActionInvocation<?> invocation;

    public SendMBRIRKey(Service service, SendMBRIRKeyInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SendMBRIRKey"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("ActivityIndex", UpnpValue.forInput(getActionInvocation(), "ActivityIndex", input.ActivityIndex));
        getActionInvocation().setInput("MBRDevice", UpnpValue.forInput(getActionInvocation(), "MBRDevice", input.MBRDevice));
        getActionInvocation().setInput("MBRIRKey", UpnpValue.forInput(getActionInvocation(), "MBRIRKey", input.MBRIRKey));
    }

    public SendMBRIRKeyOutput executeAction()
    {
        invocation = execute();

        SendMBRIRKeyOutput result = new SendMBRIRKeyOutput();

        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
