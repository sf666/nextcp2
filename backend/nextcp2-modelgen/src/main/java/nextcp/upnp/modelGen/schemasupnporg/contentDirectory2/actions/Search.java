package nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;
import nextcp.upnp.UpnpValue;

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
		
        getActionInvocation().setInput("ContainerID", UpnpValue.forInput(getActionInvocation(), "ContainerID", input.ContainerID));
        getActionInvocation().setInput("Filter", UpnpValue.forInput(getActionInvocation(), "Filter", input.Filter));
        getActionInvocation().setInput("RequestedCount", UpnpValue.forInput(getActionInvocation(), "RequestedCount", input.RequestedCount));
        getActionInvocation().setInput("SearchCriteria", UpnpValue.forInput(getActionInvocation(), "SearchCriteria", input.SearchCriteria));
        getActionInvocation().setInput("SortCriteria", UpnpValue.forInput(getActionInvocation(), "SortCriteria", input.SortCriteria));
        getActionInvocation().setInput("StartingIndex", UpnpValue.forInput(getActionInvocation(), "StartingIndex", input.StartingIndex));
    }

    public SearchOutput executeAction()
    {
        invocation = execute();

        SearchOutput result = new SearchOutput();

        result.NumberReturned = UpnpValue.toLong(invocation.getOutput("NumberReturned").getValue());
        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());
        result.TotalMatches = UpnpValue.toLong(invocation.getOutput("TotalMatches").getValue());
        result.UpdateID = UpnpValue.toLong(invocation.getOutput("UpdateID").getValue());

        return result;
    }
}
