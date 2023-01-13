package nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions;

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
public class GetPositionInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetPositionInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetPositionInfo(Service service, GetPositionInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetPositionInfo"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetPositionInfoOutput executeAction()
    {
        invocation = execute();

        GetPositionInfoOutput result = new GetPositionInfoOutput();

        result.Track = ((UnsignedIntegerFourBytes) invocation.getOutput("Track").getValue()).getValue();
  		if (invocation.getOutput("TrackDuration").getValue() != null)
  		{
	        result.TrackDuration = invocation.getOutput("TrackDuration").getValue().toString();
  		}
  		else
  		{
	        result.TrackDuration = "";
  		}
  		if (invocation.getOutput("TrackMetaData").getValue() != null)
  		{
	        result.TrackMetaData = invocation.getOutput("TrackMetaData").getValue().toString();
  		}
  		else
  		{
	        result.TrackMetaData = "";
  		}
  		if (invocation.getOutput("TrackURI").getValue() != null)
  		{
	        result.TrackURI = invocation.getOutput("TrackURI").getValue().toString();
  		}
  		else
  		{
	        result.TrackURI = "";
  		}
  		if (invocation.getOutput("RelTime").getValue() != null)
  		{
	        result.RelTime = invocation.getOutput("RelTime").getValue().toString();
  		}
  		else
  		{
	        result.RelTime = "";
  		}
  		if (invocation.getOutput("AbsTime").getValue() != null)
  		{
	        result.AbsTime = invocation.getOutput("AbsTime").getValue().toString();
  		}
  		else
  		{
	        result.AbsTime = "";
  		}
        result.RelCount = Integer.valueOf(invocation.getOutput("RelCount").getValue().toString());
        result.AbsCount = Integer.valueOf(invocation.getOutput("AbsCount").getValue().toString());

        return result;
    }
}
