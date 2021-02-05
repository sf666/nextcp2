package nextcp.upnp.modelGen.jminimorg.monitor2.actions;

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
public class GetProperty extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetProperty.class.getName());
    private ActionInvocation<?> invocation;

    public GetProperty(Service service, GetPropertyInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetProperty"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ComponentName", input.ComponentName);
        getActionInvocation().setInput("PropertyName", input.PropertyName);
    }

    public GetPropertyOutput executeAction()
    {
        invocation = execute();

        GetPropertyOutput result = new GetPropertyOutput();

  		if (invocation.getOutput("PropertyValue").getValue() != null)
  		{
	        result.PropertyValue = invocation.getOutput("PropertyValue").getValue().toString();
  		}
  		else
  		{
	        result.PropertyValue = "";
  		}

        return result;
    }
}
