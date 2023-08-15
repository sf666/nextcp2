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
public class GetCurrentProgramInformationURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetCurrentProgramInformationURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetCurrentProgramInformationURL(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetCurrentProgramInformationURL"), new NextcpClientInfo()), cp);

    }

    public GetCurrentProgramInformationURLOutput executeAction()
    {
        invocation = execute();

        GetCurrentProgramInformationURLOutput result = new GetCurrentProgramInformationURLOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("CurrentProgInfoURL").getValue() != null)
  		{
	        result.CurrentProgInfoURL = invocation.getOutput("CurrentProgInfoURL").getValue().toString();
  		}
  		else
  		{
	        result.CurrentProgInfoURL = "";
  		}

        return result;
    }
}
