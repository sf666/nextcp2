package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

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
public class X_UpdateAudioSelection extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_UpdateAudioSelection.class.getName());
    private ActionInvocation<?> invocation;

    public X_UpdateAudioSelection(Service service, X_UpdateAudioSelectionInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_UpdateAudioSelection"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("AudioPID", new UnsignedIntegerTwoBytes(input.AudioPID));
        getActionInvocation().setInput("AudioEncoding", input.AudioEncoding);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
