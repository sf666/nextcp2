package nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions;

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
public class GetProtocolInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetProtocolInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetProtocolInfo(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetProtocolInfo"), new NextcpClientInfo()), cp);

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
