package nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions;

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
public class GetSupportedServices extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSupportedServices.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

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
