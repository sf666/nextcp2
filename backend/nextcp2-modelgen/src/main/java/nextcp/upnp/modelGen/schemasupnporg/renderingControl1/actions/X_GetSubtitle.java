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
public class X_GetSubtitle extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_GetSubtitle.class.getName());
    private ActionInvocation<?> invocation;

    public X_GetSubtitle(Service service, X_GetSubtitleInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_GetSubtitle"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public X_GetSubtitleOutput executeAction()
    {
        invocation = execute();

        X_GetSubtitleOutput result = new X_GetSubtitleOutput();

  		if (invocation.getOutput("CurrentSubtitle").getValue() != null)
  		{
	        result.CurrentSubtitle = invocation.getOutput("CurrentSubtitle").getValue().toString();
  		}
  		else
  		{
	        result.CurrentSubtitle = "";
  		}

        return result;
    }
}
