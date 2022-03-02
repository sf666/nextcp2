package nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions;

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
public class GetTransportSettings extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetTransportSettings.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public GetTransportSettings(Service service, GetTransportSettingsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetTransportSettings"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetTransportSettingsOutput executeAction()
    {
        invocation = execute();

        GetTransportSettingsOutput result = new GetTransportSettingsOutput();

  		if (invocation.getOutput("PlayMode").getValue() != null)
  		{
	        result.PlayMode = invocation.getOutput("PlayMode").getValue().toString();
  		}
  		else
  		{
	        result.PlayMode = "";
  		}
  		if (invocation.getOutput("RecQualityMode").getValue() != null)
  		{
	        result.RecQualityMode = invocation.getOutput("RecQualityMode").getValue().toString();
  		}
  		else
  		{
	        result.RecQualityMode = "";
  		}

        return result;
    }
}
