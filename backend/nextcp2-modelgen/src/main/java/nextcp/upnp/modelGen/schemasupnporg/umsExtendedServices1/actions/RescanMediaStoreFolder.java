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

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class RescanMediaStoreFolder extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(RescanMediaStoreFolder.class.getName());
    private ActionInvocation<?> invocation;

    public RescanMediaStoreFolder(Service service, RescanMediaStoreFolderInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("RescanMediaStoreFolder"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ObjectID", input.ObjectID);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
