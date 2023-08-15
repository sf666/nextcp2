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
public class X_GetAudioSelection extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_GetAudioSelection.class.getName());
    private ActionInvocation<?> invocation;

    public X_GetAudioSelection(Service service, X_GetAudioSelectionInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_GetAudioSelection"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public X_GetAudioSelectionOutput executeAction()
    {
        invocation = execute();

        X_GetAudioSelectionOutput result = new X_GetAudioSelectionOutput();

        result.AudioPID = ((UnsignedIntegerTwoBytes) invocation.getOutput("AudioPID").getValue()).getValue();
  		if (invocation.getOutput("AudioEncoding").getValue() != null)
  		{
	        result.AudioEncoding = invocation.getOutput("AudioEncoding").getValue().toString();
  		}
  		else
  		{
	        result.AudioEncoding = "";
  		}

        return result;
    }
}
