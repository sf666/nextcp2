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
public class SetWebStreamIcyOrder extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetWebStreamIcyOrder.class.getName());
    private ActionInvocation<?> invocation;

    public SetWebStreamIcyOrder(Service service, SetWebStreamIcyOrderInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetWebStreamIcyOrder"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("IcyOrder", UpnpValue.forInput(getActionInvocation(), "IcyOrder", input.IcyOrder));
        getActionInvocation().setInput("ObjectID", UpnpValue.forInput(getActionInvocation(), "ObjectID", input.ObjectID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
