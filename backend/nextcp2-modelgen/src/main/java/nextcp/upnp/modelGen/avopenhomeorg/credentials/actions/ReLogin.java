package nextcp.upnp.modelGen.avopenhomeorg.credentials.actions;

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
public class ReLogin extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ReLogin.class.getName());
    private ActionInvocation<?> invocation;

    public ReLogin(Service service, ReLoginInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ReLogin"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Id", input.Id);
        getActionInvocation().setInput("CurrentToken", input.CurrentToken);
    }

    public ReLoginOutput executeAction()
    {
        invocation = execute();

        ReLoginOutput result = new ReLoginOutput();

  		if (invocation.getOutput("NewToken").getValue() != null)
  		{
	        result.NewToken = invocation.getOutput("NewToken").getValue().toString();
  		}
  		else
  		{
	        result.NewToken = "";
  		}

        return result;
    }
}
