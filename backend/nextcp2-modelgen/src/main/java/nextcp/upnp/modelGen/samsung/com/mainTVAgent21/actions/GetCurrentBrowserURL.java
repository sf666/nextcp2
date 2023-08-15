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

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class GetCurrentBrowserURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetCurrentBrowserURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetCurrentBrowserURL(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetCurrentBrowserURL"), new NextcpClientInfo()), cp);

    }

    public GetCurrentBrowserURLOutput executeAction()
    {
        invocation = execute();

        GetCurrentBrowserURLOutput result = new GetCurrentBrowserURLOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("BrowserURL").getValue() != null)
  		{
	        result.BrowserURL = invocation.getOutput("BrowserURL").getValue().toString();
  		}
  		else
  		{
	        result.BrowserURL = "";
  		}

        return result;
    }
}
