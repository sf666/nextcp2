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
public class SetAudioAddictPass extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetAudioAddictPass.class.getName());
    private ActionInvocation<?> invocation;

    public SetAudioAddictPass(Service service, SetAudioAddictPassInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetAudioAddictPass"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("AudioAddictPass", input.AudioAddictPass);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
