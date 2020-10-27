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
public class GetPackageStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetPackageStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetPackageStatus(Service service, GetPackageStatusInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetPackageStatus")), cp);

        getActionInvocation().setInput("PackageName", input.PackageName);
    }

    public GetPackageStatusOutput executeAction()
    {
        invocation = execute();

        GetPackageStatusOutput result = new GetPackageStatusOutput();

  		if (invocation.getOutput("PackageStatus").getValue() != null)
  		{
	        result.PackageStatus = invocation.getOutput("PackageStatus").getValue().toString();
  		}
  		else
  		{
	        result.PackageStatus = "";
  		}

        return result;
    }
}
