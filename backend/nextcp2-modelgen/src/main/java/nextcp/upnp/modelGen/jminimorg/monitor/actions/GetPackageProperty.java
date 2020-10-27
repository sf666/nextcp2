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
public class GetPackageProperty extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetPackageProperty.class.getName());
    private ActionInvocation<?> invocation;

    public GetPackageProperty(Service service, GetPackagePropertyInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetPackageProperty")), cp);

        getActionInvocation().setInput("PackageName", input.PackageName);
        getActionInvocation().setInput("PropertyName", input.PropertyName);
    }

    public GetPackagePropertyOutput executeAction()
    {
        invocation = execute();

        GetPackagePropertyOutput result = new GetPackagePropertyOutput();

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
