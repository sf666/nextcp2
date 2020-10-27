package nextcp.upnp.modelGen.jminimorg.monitor.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class SetProperty extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetProperty.class.getName());
    private ActionInvocation<?> invocation;

    public SetProperty(Service service, SetPropertyInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetProperty")), cp);

        getActionInvocation().setInput("ComponentName", input.ComponentName);
        getActionInvocation().setInput("PropertyName", input.PropertyName);
        getActionInvocation().setInput("PropertyValue", input.PropertyValue);
    }

    public SetPropertyOutput executeAction()
    {
        invocation = execute();

        SetPropertyOutput result = new SetPropertyOutput();

  		if (invocation.getOutput("ErrorMsg").getValue() != null)
  		{
	        result.ErrorMsg = invocation.getOutput("ErrorMsg").getValue().toString();
  		}
  		else
  		{
	        result.ErrorMsg = "";
  		}

        return result;
    }
}
