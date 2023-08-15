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
public class GetMBRDeviceList extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMBRDeviceList.class.getName());
    private ActionInvocation<?> invocation;

    public GetMBRDeviceList(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMBRDeviceList"), new NextcpClientInfo()), cp);

    }

    public GetMBRDeviceListOutput executeAction()
    {
        invocation = execute();

        GetMBRDeviceListOutput result = new GetMBRDeviceListOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("MBRDeviceList").getValue() != null)
  		{
	        result.MBRDeviceList = invocation.getOutput("MBRDeviceList").getValue().toString();
  		}
  		else
  		{
	        result.MBRDeviceList = "";
  		}

        return result;
    }
}
