package nextcp.upnp.modelGen.avopenhomeorg.product1.actions;

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
public class Model extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Model.class.getName());
    private ActionInvocation<?> invocation;

    public Model(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Model"), new NextcpClientInfo()), cp);
		
    }

    public ModelOutput executeAction()
    {
        invocation = execute();

        ModelOutput result = new ModelOutput();

        result.ImageUri = UpnpValue.toTextOrEmpty(invocation.getOutput("ImageUri").getValue());
        result.Info = UpnpValue.toTextOrEmpty(invocation.getOutput("Info").getValue());
        result.Name = UpnpValue.toTextOrEmpty(invocation.getOutput("Name").getValue());
        result.Url = UpnpValue.toTextOrEmpty(invocation.getOutput("Url").getValue());

        return result;
    }
}
