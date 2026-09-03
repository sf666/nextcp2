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
public class CreateReference extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(CreateReference.class.getName());
    private ActionInvocation<?> invocation;

    public CreateReference(Service service, CreateReferenceInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("CreateReference"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("ContainerID", UpnpValue.forInput(getActionInvocation(), "ContainerID", input.ContainerID));
        getActionInvocation().setInput("ObjectID", UpnpValue.forInput(getActionInvocation(), "ObjectID", input.ObjectID));
    }

    public CreateReferenceOutput executeAction()
    {
        invocation = execute();

        CreateReferenceOutput result = new CreateReferenceOutput();

        result.NewID = UpnpValue.toTextOrEmpty(invocation.getOutput("NewID").getValue());

        return result;
    }
}
