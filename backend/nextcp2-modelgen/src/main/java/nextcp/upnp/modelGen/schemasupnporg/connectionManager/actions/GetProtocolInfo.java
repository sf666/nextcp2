package nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class GetProtocolInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetProtocolInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetProtocolInfo(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetProtocolInfo")), cp);

    }

    public GetProtocolInfoOutput executeAction()
    {
        invocation = execute();

        GetProtocolInfoOutput result = new GetProtocolInfoOutput();

  		if (invocation.getOutput("Source").getValue() != null)
  		{
	        result.Source = invocation.getOutput("Source").getValue().toString();
  		}
  		else
  		{
	        result.Source = "";
  		}
  		if (invocation.getOutput("Sink").getValue() != null)
  		{
	        result.Sink = invocation.getOutput("Sink").getValue().toString();
  		}
  		else
  		{
	        result.Sink = "";
  		}

        return result;
    }
}
