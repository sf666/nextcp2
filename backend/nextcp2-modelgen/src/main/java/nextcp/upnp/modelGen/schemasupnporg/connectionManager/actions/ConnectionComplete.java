package nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions;

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
public class ConnectionComplete extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ConnectionComplete.class.getName());
    private ActionInvocation<?> invocation;

    public ConnectionComplete(Service service, ConnectionCompleteInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ConnectionComplete")), cp);

        getActionInvocation().setInput("ConnectionID", new IntegerDatatype(input.ConnectionID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
