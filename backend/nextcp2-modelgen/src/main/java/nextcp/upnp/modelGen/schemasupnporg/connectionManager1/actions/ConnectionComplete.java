package nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions;

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
public class ConnectionComplete extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ConnectionComplete.class.getName());
    private ActionInvocation<?> invocation;

    public ConnectionComplete(Service service, ConnectionCompleteInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ConnectionComplete"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ConnectionID", new IntegerDatatype(input.ConnectionID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
