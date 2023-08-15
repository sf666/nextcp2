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
public class GetChannelListURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetChannelListURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetChannelListURL(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetChannelListURL"), new NextcpClientInfo()), cp);

    }

    public GetChannelListURLOutput executeAction()
    {
        invocation = execute();

        GetChannelListURLOutput result = new GetChannelListURLOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
        result.ChannelListVersion = ((UnsignedIntegerFourBytes) invocation.getOutput("ChannelListVersion").getValue()).getValue();
  		if (invocation.getOutput("SupportChannelList").getValue() != null)
  		{
	        result.SupportChannelList = invocation.getOutput("SupportChannelList").getValue().toString();
  		}
  		else
  		{
	        result.SupportChannelList = "";
  		}
  		if (invocation.getOutput("ChannelListURL").getValue() != null)
  		{
	        result.ChannelListURL = invocation.getOutput("ChannelListURL").getValue().toString();
  		}
  		else
  		{
	        result.ChannelListURL = "";
  		}
  		if (invocation.getOutput("ChannelListType").getValue() != null)
  		{
	        result.ChannelListType = invocation.getOutput("ChannelListType").getValue().toString();
  		}
  		else
  		{
	        result.ChannelListType = "";
  		}
        result.SatelliteID = ((UnsignedIntegerFourBytes) invocation.getOutput("SatelliteID").getValue()).getValue();

        return result;
    }
}
