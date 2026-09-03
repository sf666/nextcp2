package nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions;

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
public class BeginLimitedInputFlow extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(BeginLimitedInputFlow.class.getName());
    private ActionInvocation<?> invocation;

    public BeginLimitedInputFlow(Service service, BeginLimitedInputFlowInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("BeginLimitedInputFlow"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("ServiceId", UpnpValue.forInput(getActionInvocation(), "ServiceId", input.ServiceId));
    }

    public BeginLimitedInputFlowOutput executeAction()
    {
        invocation = execute();

        BeginLimitedInputFlowOutput result = new BeginLimitedInputFlowOutput();

        result.JobId = UpnpValue.toTextOrEmpty(invocation.getOutput("JobId").getValue());
        result.LoginUrl = UpnpValue.toTextOrEmpty(invocation.getOutput("LoginUrl").getValue());
        result.UserCode = UpnpValue.toTextOrEmpty(invocation.getOutput("UserCode").getValue());

        return result;
    }
}
