package nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions;

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
public class Insert extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Insert.class.getName());
    private ActionInvocation<?> invocation;

    public Insert(Service service, InsertInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Insert"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("AfterId", UpnpValue.forInput(getActionInvocation(), "AfterId", input.AfterId));
        getActionInvocation().setInput("Metadata", UpnpValue.forInput(getActionInvocation(), "Metadata", input.Metadata));
        getActionInvocation().setInput("Uri", UpnpValue.forInput(getActionInvocation(), "Uri", input.Uri));
    }

    public InsertOutput executeAction()
    {
        invocation = execute();

        InsertOutput result = new InsertOutput();

        result.NewId = UpnpValue.toLong(invocation.getOutput("NewId").getValue());

        return result;
    }
}
