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
public class GetAllPackageStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAllPackageStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetAllPackageStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAllPackageStatus")), cp);

    }

    public GetAllPackageStatusOutput executeAction()
    {
        invocation = execute();

        GetAllPackageStatusOutput result = new GetAllPackageStatusOutput();

  		if (invocation.getOutput("PackageStatusList").getValue() != null)
  		{
	        result.PackageStatusList = invocation.getOutput("PackageStatusList").getValue().toString();
  		}
  		else
  		{
	        result.PackageStatusList = "";
  		}

        return result;
    }
}
