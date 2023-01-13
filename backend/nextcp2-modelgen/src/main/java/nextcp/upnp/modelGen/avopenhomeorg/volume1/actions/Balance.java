package nextcp.upnp.modelGen.avopenhomeorg.volume1.actions;

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
public class Balance extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Balance.class.getName());
    private ActionInvocation<?> invocation;

    public Balance(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Balance"), new NextcpClientInfo()), cp);

    }

    public BalanceOutput executeAction()
    {
        invocation = execute();

        BalanceOutput result = new BalanceOutput();

        result.Value = Integer.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
