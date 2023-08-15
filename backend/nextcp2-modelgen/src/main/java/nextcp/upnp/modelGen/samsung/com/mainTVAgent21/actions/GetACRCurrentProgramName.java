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
public class GetACRCurrentProgramName extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetACRCurrentProgramName.class.getName());
    private ActionInvocation<?> invocation;

    public GetACRCurrentProgramName(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetACRCurrentProgramName"), new NextcpClientInfo()), cp);

    }

    public GetACRCurrentProgramNameOutput executeAction()
    {
        invocation = execute();

        GetACRCurrentProgramNameOutput result = new GetACRCurrentProgramNameOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("ProgramName").getValue() != null)
  		{
	        result.ProgramName = invocation.getOutput("ProgramName").getValue().toString();
  		}
  		else
  		{
	        result.ProgramName = "";
  		}

        return result;
    }
}
