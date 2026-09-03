package nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions;

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
public class GetHTSSoundEffect extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetHTSSoundEffect.class.getName());
    private ActionInvocation<?> invocation;

    public GetHTSSoundEffect(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetHTSSoundEffect"), new NextcpClientInfo()), cp);
		
    }

    public GetHTSSoundEffectOutput executeAction()
    {
        invocation = execute();

        GetHTSSoundEffectOutput result = new GetHTSSoundEffectOutput();

        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());
        result.SoundEffect = UpnpValue.toTextOrEmpty(invocation.getOutput("SoundEffect").getValue());
        result.SoundEffectList = UpnpValue.toTextOrEmpty(invocation.getOutput("SoundEffectList").getValue());

        return result;
    }
}
