package nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions;

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

        getActionInvocation().setInput("ServiceId", input.ServiceId);
    }

    public BeginLimitedInputFlowOutput executeAction()
    {
        invocation = execute();

        BeginLimitedInputFlowOutput result = new BeginLimitedInputFlowOutput();

  		if (invocation.getOutput("JobId").getValue() != null)
  		{
	        result.JobId = invocation.getOutput("JobId").getValue().toString();
  		}
  		else
  		{
	        result.JobId = "";
  		}
  		if (invocation.getOutput("LoginUrl").getValue() != null)
  		{
	        result.LoginUrl = invocation.getOutput("LoginUrl").getValue().toString();
  		}
  		else
  		{
	        result.LoginUrl = "";
  		}
  		if (invocation.getOutput("UserCode").getValue() != null)
  		{
	        result.UserCode = invocation.getOutput("UserCode").getValue().toString();
  		}
  		else
  		{
	        result.UserCode = "";
  		}

        return result;
    }
}
