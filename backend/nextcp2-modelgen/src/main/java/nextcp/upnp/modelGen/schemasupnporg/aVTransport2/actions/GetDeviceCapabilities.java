package nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions;

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
public class GetDeviceCapabilities extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetDeviceCapabilities.class.getName());
    private ActionInvocation<?> invocation;

    public GetDeviceCapabilities(Service service, GetDeviceCapabilitiesInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetDeviceCapabilities"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetDeviceCapabilitiesOutput executeAction()
    {
        invocation = execute();

        GetDeviceCapabilitiesOutput result = new GetDeviceCapabilitiesOutput();

  		if (invocation.getOutput("PlayMedia").getValue() != null)
  		{
	        result.PlayMedia = invocation.getOutput("PlayMedia").getValue().toString();
  		}
  		else
  		{
	        result.PlayMedia = "";
  		}
  		if (invocation.getOutput("RecMedia").getValue() != null)
  		{
	        result.RecMedia = invocation.getOutput("RecMedia").getValue().toString();
  		}
  		else
  		{
	        result.RecMedia = "";
  		}
  		if (invocation.getOutput("RecQualityModes").getValue() != null)
  		{
	        result.RecQualityModes = invocation.getOutput("RecQualityModes").getValue().toString();
  		}
  		else
  		{
	        result.RecQualityModes = "";
  		}

        return result;
    }
}
