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
public class GetServiceStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetServiceStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetServiceStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetServiceStatus"), new NextcpClientInfo()), cp);

    }

    public GetServiceStatusOutput executeAction()
    {
        invocation = execute();

        GetServiceStatusOutput result = new GetServiceStatusOutput();

  		if (invocation.getOutput("ServiceStatusJson").getValue() != null)
  		{
	        result.ServiceStatusJson = invocation.getOutput("ServiceStatusJson").getValue().toString();
  		}
  		else
  		{
	        result.ServiceStatusJson = "";
  		}

        return result;
    }
}