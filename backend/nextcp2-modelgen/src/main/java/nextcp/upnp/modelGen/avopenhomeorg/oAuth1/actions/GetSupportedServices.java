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

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class GetSupportedServices extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSupportedServices.class.getName());
    private ActionInvocation<?> invocation;

    public GetSupportedServices(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSupportedServices"), new NextcpClientInfo()), cp);

    }

    public GetSupportedServicesOutput executeAction()
    {
        invocation = execute();

        GetSupportedServicesOutput result = new GetSupportedServicesOutput();

  		if (invocation.getOutput("SupportedServices").getValue() != null)
  		{
	        result.SupportedServices = invocation.getOutput("SupportedServices").getValue().toString();
  		}
  		else
  		{
	        result.SupportedServices = "";
  		}

        return result;
    }
}
