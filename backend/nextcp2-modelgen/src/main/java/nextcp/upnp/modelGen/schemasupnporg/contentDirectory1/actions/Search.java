package nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions;

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
public class Search extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Search.class.getName());
    private ActionInvocation<?> invocation;

    public Search(Service service, SearchInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Search"), new NextcpClientInfo()), cp);
		
        if (input.ContainerID != null) {
	        getActionInvocation().setInput("ContainerID", input.ContainerID);
		} else {
    	    getActionInvocation().setInput("ContainerID", null);
		}
        if (input.SearchCriteria != null) {
	        getActionInvocation().setInput("SearchCriteria", input.SearchCriteria);
		} else {
    	    getActionInvocation().setInput("SearchCriteria", null);
		}
        if (input.Filter != null) {
	        getActionInvocation().setInput("Filter", input.Filter);
		} else {
    	    getActionInvocation().setInput("Filter", null);
		}
        if (input.StartingIndex != null) {
    	    getActionInvocation().setInput("StartingIndex", new UnsignedIntegerFourBytes(input.StartingIndex));
		} else {
    	    getActionInvocation().setInput("StartingIndex", null);
		}
        if (input.RequestedCount != null) {
    	    getActionInvocation().setInput("RequestedCount", new UnsignedIntegerFourBytes(input.RequestedCount));
		} else {
    	    getActionInvocation().setInput("RequestedCount", null);
		}
        if (input.SortCriteria != null) {
	        getActionInvocation().setInput("SortCriteria", input.SortCriteria);
		} else {
    	    getActionInvocation().setInput("SortCriteria", null);
		}
    }

    public SearchOutput executeAction()
    {
        invocation = execute();

        SearchOutput result = new SearchOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
        result.NumberReturned = ((UnsignedIntegerFourBytes) invocation.getOutput("NumberReturned").getValue()).getValue();
        result.TotalMatches = ((UnsignedIntegerFourBytes) invocation.getOutput("TotalMatches").getValue()).getValue();
        result.UpdateID = ((UnsignedIntegerFourBytes) invocation.getOutput("UpdateID").getValue()).getValue();

        return result;
    }
}
