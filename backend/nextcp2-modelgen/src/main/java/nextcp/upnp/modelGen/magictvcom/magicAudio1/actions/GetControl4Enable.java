package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class GetControl4Enable extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetControl4Enable.class.getName());
    private ActionInvocation<?> invocation;

    public GetControl4Enable(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetControl4Enable"), new NextcpClientInfo()), cp);
		
    }

    public GetControl4EnableOutput executeAction()
    {
        invocation = execute();

        GetControl4EnableOutput result = new GetControl4EnableOutput();

        result.Value = Integer.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
