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
public class GetAllProgramInformationURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAllProgramInformationURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetAllProgramInformationURL(Service service, GetAllProgramInformationURLInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAllProgramInformationURL"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("AntennaMode", new UnsignedIntegerFourBytes(input.AntennaMode));
        getActionInvocation().setInput("Channel", input.Channel);
    }

    public GetAllProgramInformationURLOutput executeAction()
    {
        invocation = execute();

        GetAllProgramInformationURLOutput result = new GetAllProgramInformationURLOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("AllProgramInformationURL").getValue() != null)
  		{
	        result.AllProgramInformationURL = invocation.getOutput("AllProgramInformationURL").getValue().toString();
  		}
  		else
  		{
	        result.AllProgramInformationURL = "";
  		}

        return result;
    }
}
