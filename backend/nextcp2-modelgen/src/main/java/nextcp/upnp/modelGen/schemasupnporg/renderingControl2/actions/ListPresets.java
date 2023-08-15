package nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions;

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
public class ListPresets extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ListPresets.class.getName());
    private ActionInvocation<?> invocation;

    public ListPresets(Service service, ListPresetsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ListPresets"), new NextcpClientInfo()), cp);

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
