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
public class SaveProperty extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SaveProperty.class.getName());
    private ActionInvocation<?> invocation;

    public SaveProperty(Service service, SavePropertyInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SaveProperty")), cp);

        getActionInvocation().setInput("ComponentName", input.ComponentName);
        getActionInvocation().setInput("PropertyName", input.PropertyName);
        getActionInvocation().setInput("PropertyValue", input.PropertyValue);
    }

    public SavePropertyOutput executeAction()
    {
        invocation = execute();

        SavePropertyOutput result = new SavePropertyOutput();

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
