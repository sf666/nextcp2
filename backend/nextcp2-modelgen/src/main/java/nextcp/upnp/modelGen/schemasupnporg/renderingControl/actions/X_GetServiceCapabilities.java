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
public class X_GetServiceCapabilities extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_GetServiceCapabilities.class.getName());
    private ActionInvocation<?> invocation;

    public X_GetServiceCapabilities(Service service, X_GetServiceCapabilitiesInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_GetServiceCapabilities"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public X_GetServiceCapabilitiesOutput executeAction()
    {
        invocation = execute();

        X_GetServiceCapabilitiesOutput result = new X_GetServiceCapabilitiesOutput();

  		if (invocation.getOutput("ServiceCapabilities").getValue() != null)
  		{
	        result.ServiceCapabilities = invocation.getOutput("ServiceCapabilities").getValue().toString();
  		}
  		else
  		{
	        result.ServiceCapabilities = "";
  		}

        return result;
    }
}
