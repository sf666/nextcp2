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
public class GetMBRDongleStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMBRDongleStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetMBRDongleStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMBRDongleStatus"), new NextcpClientInfo()), cp);

    }

    public GetMBRDongleStatusOutput executeAction()
    {
        invocation = execute();

        GetMBRDongleStatusOutput result = new GetMBRDongleStatusOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("MBRDongleStatus").getValue() != null)
  		{
	        result.MBRDongleStatus = invocation.getOutput("MBRDongleStatus").getValue().toString();
  		}
  		else
  		{
	        result.MBRDongleStatus = "";
  		}

        return result;
    }
}
