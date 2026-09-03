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
import nextcp.upnp.UpnpValue;

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
		
        getActionInvocation().setInput("CountryCode", UpnpValue.forInput(getActionInvocation(), "CountryCode", input.CountryCode));
        getActionInvocation().setInput("Language", UpnpValue.forInput(getActionInvocation(), "Language", input.Language));
        getActionInvocation().setInput("Limit", UpnpValue.forInput(getActionInvocation(), "Limit", input.Limit));
        getActionInvocation().setInput("Name", UpnpValue.forInput(getActionInvocation(), "Name", input.Name));
        getActionInvocation().setInput("Offset", UpnpValue.forInput(getActionInvocation(), "Offset", input.Offset));
        getActionInvocation().setInput("Tag", UpnpValue.forInput(getActionInvocation(), "Tag", input.Tag));
    }

    public SearchRadioStationsOutput executeAction()
    {
        invocation = execute();

        SearchRadioStationsOutput result = new SearchRadioStationsOutput();

        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
