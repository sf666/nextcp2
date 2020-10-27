package nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions;

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
public class ListPresets extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ListPresets.class.getName());
    private ActionInvocation<?> invocation;

    public ListPresets(Service service, ListPresetsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ListPresets")), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public ListPresetsOutput executeAction()
    {
        invocation = execute();

        ListPresetsOutput result = new ListPresetsOutput();

  		if (invocation.getOutput("CurrentPresetNameList").getValue() != null)
  		{
	        result.CurrentPresetNameList = invocation.getOutput("CurrentPresetNameList").getValue().toString();
  		}
  		else
  		{
	        result.CurrentPresetNameList = "";
  		}

        return result;
    }
}
