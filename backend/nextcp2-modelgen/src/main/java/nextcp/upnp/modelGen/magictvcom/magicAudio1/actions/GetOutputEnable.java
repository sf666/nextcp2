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
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class GetOutputEnable extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetOutputEnable.class.getName());
    private ActionInvocation<?> invocation;

    public GetOutputEnable(Service service, GetOutputEnableInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetOutputEnable"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Port", UpnpValue.forInput(getActionInvocation(), "Port", input.Port));
    }

    public GetOutputEnableOutput executeAction()
    {
        invocation = execute();

        GetOutputEnableOutput result = new GetOutputEnableOutput();

        result.Value = UpnpValue.toTextOrEmpty(invocation.getOutput("Value").getValue());

        return result;
    }
}
