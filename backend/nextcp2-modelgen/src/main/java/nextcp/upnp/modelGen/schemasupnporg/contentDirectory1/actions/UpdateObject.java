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
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class UpdateObject extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(UpdateObject.class.getName());
    private ActionInvocation<?> invocation;

    public UpdateObject(Service service, UpdateObjectInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("UpdateObject"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("CurrentTagValue", UpnpValue.forInput(getActionInvocation(), "CurrentTagValue", input.CurrentTagValue));
        getActionInvocation().setInput("NewTagValue", UpnpValue.forInput(getActionInvocation(), "NewTagValue", input.NewTagValue));
        getActionInvocation().setInput("ObjectID", UpnpValue.forInput(getActionInvocation(), "ObjectID", input.ObjectID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
