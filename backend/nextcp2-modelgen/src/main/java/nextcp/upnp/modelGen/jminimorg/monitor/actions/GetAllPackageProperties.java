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
public class GetAllPackageProperties extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAllPackageProperties.class.getName());
    private ActionInvocation<?> invocation;

    public GetAllPackageProperties(Service service, GetAllPackagePropertiesInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAllPackageProperties")), cp);

        getActionInvocation().setInput("PropertyNames", input.PropertyNames);
    }

    public GetAllPackagePropertiesOutput executeAction()
    {
        invocation = execute();

        GetAllPackagePropertiesOutput result = new GetAllPackagePropertiesOutput();

  		if (invocation.getOutput("PackagePropertyList").getValue() != null)
  		{
	        result.PackagePropertyList = invocation.getOutput("PackagePropertyList").getValue().toString();
  		}
  		else
  		{
	        result.PackagePropertyList = "";
  		}

        return result;
    }
}
