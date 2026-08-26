package nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions;

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
public class GetRadioFilterValues extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRadioFilterValues.class.getName());
    private ActionInvocation<?> invocation;

    public GetRadioFilterValues(Service service, GetRadioFilterValuesInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRadioFilterValues"), new NextcpClientInfo()), cp);
		
        if (input.Kind != null) {
	        getActionInvocation().setInput("Kind", input.Kind);
		} else {
    	    getActionInvocation().setInput("Kind", null);
		}
        if (input.Search != null) {
	        getActionInvocation().setInput("Search", input.Search);
		} else {
    	    getActionInvocation().setInput("Search", null);
		}
    }

    public GetRadioFilterValuesOutput executeAction()
    {
        invocation = execute();

        GetRadioFilterValuesOutput result = new GetRadioFilterValuesOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}

        return result;
    }
}
