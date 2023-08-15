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
public class GetRecordChannel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRecordChannel.class.getName());
    private ActionInvocation<?> invocation;

    public GetRecordChannel(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRecordChannel"), new NextcpClientInfo()), cp);

    }

    public GetRecordChannelOutput executeAction()
    {
        invocation = execute();

        GetRecordChannelOutput result = new GetRecordChannelOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("RecordChannel").getValue() != null)
  		{
	        result.RecordChannel = invocation.getOutput("RecordChannel").getValue().toString();
  		}
  		else
  		{
	        result.RecordChannel = "";
  		}
  		if (invocation.getOutput("RecordChannel2").getValue() != null)
  		{
	        result.RecordChannel2 = invocation.getOutput("RecordChannel2").getValue().toString();
  		}
  		else
  		{
	        result.RecordChannel2 = "";
  		}

        return result;
    }
}
