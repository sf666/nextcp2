package nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class X_GetAspectRatio extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_GetAspectRatio.class.getName());
    private ActionInvocation<?> invocation;

    public X_GetAspectRatio(Service service, X_GetAspectRatioInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_GetAspectRatio"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public X_GetAspectRatioOutput executeAction()
    {
        invocation = execute();

        X_GetAspectRatioOutput result = new X_GetAspectRatioOutput();

  		if (invocation.getOutput("AspectRatio").getValue() != null)
  		{
	        result.AspectRatio = invocation.getOutput("AspectRatio").getValue().toString();
  		}
  		else
  		{
	        result.AspectRatio = "";
  		}

        return result;
    }
}
