package nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class Search extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Search.class.getName());
    private ActionInvocation<?> invocation;

    public Search(Service service, SearchInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Search")), cp);

        getActionInvocation().setInput("ContainerID", input.ContainerID);
        getActionInvocation().setInput("SearchCriteria", input.SearchCriteria);
        getActionInvocation().setInput("Filter", input.Filter);
        getActionInvocation().setInput("StartingIndex", new UnsignedIntegerFourBytes(input.StartingIndex));
        getActionInvocation().setInput("RequestedCount", new UnsignedIntegerFourBytes(input.RequestedCount));
        getActionInvocation().setInput("SortCriteria", input.SortCriteria);
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
