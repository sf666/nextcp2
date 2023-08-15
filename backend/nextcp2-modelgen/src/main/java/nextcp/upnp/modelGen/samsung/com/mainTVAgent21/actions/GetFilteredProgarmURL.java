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
public class GetFilteredProgarmURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetFilteredProgarmURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetFilteredProgarmURL(Service service, GetFilteredProgarmURLInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetFilteredProgarmURL"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Keyword", input.Keyword);
    }

    public GetFilteredProgarmURLOutput executeAction()
    {
        invocation = execute();

        GetFilteredProgarmURLOutput result = new GetFilteredProgarmURLOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("FilteredProgramURL").getValue() != null)
  		{
	        result.FilteredProgramURL = invocation.getOutput("FilteredProgramURL").getValue().toString();
  		}
  		else
  		{
	        result.FilteredProgramURL = "";
  		}

        return result;
    }
}
