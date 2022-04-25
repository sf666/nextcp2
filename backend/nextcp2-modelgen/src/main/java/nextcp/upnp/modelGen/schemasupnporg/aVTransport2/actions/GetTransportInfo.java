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
public class GetTransportInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetTransportInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetTransportInfo(Service service, GetTransportInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetTransportInfo"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetTransportInfoOutput executeAction()
    {
        invocation = execute();

        GetTransportInfoOutput result = new GetTransportInfoOutput();

  		if (invocation.getOutput("CurrentTransportState").getValue() != null)
  		{
	        result.CurrentTransportState = invocation.getOutput("CurrentTransportState").getValue().toString();
  		}
  		else
  		{
	        result.CurrentTransportState = "";
  		}
  		if (invocation.getOutput("CurrentTransportStatus").getValue() != null)
  		{
	        result.CurrentTransportStatus = invocation.getOutput("CurrentTransportStatus").getValue().toString();
  		}
  		else
  		{
	        result.CurrentTransportStatus = "";
  		}
  		if (invocation.getOutput("CurrentSpeed").getValue() != null)
  		{
	        result.CurrentSpeed = invocation.getOutput("CurrentSpeed").getValue().toString();
  		}
  		else
  		{
	        result.CurrentSpeed = "";
  		}

        return result;
    }
}
