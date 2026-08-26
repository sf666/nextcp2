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
public class SearchRadioStations extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SearchRadioStations.class.getName());
    private ActionInvocation<?> invocation;

    public SearchRadioStations(Service service, SearchRadioStationsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SearchRadioStations"), new NextcpClientInfo()), cp);
		
        if (input.Name != null) {
	        getActionInvocation().setInput("Name", input.Name);
		} else {
    	    getActionInvocation().setInput("Name", null);
		}
        if (input.CountryCode != null) {
	        getActionInvocation().setInput("CountryCode", input.CountryCode);
		} else {
    	    getActionInvocation().setInput("CountryCode", null);
		}
        if (input.Language != null) {
	        getActionInvocation().setInput("Language", input.Language);
		} else {
    	    getActionInvocation().setInput("Language", null);
		}
        if (input.Tag != null) {
	        getActionInvocation().setInput("Tag", input.Tag);
		} else {
    	    getActionInvocation().setInput("Tag", null);
		}
        if (input.Offset != null) {
    	    getActionInvocation().setInput("Offset", new UnsignedIntegerFourBytes(input.Offset));
		} else {
    	    getActionInvocation().setInput("Offset", null);
		}
        if (input.Limit != null) {
    	    getActionInvocation().setInput("Limit", new UnsignedIntegerFourBytes(input.Limit));
		} else {
    	    getActionInvocation().setInput("Limit", null);
		}
    }

    public SearchRadioStationsOutput executeAction()
    {
        invocation = execute();

        SearchRadioStationsOutput result = new SearchRadioStationsOutput();

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
