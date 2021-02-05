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
public class GetPackageAboutInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetPackageAboutInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetPackageAboutInfo(Service service, GetPackageAboutInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetPackageAboutInfo"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("PackageName", input.PackageName);
    }

    public GetPackageAboutInfoOutput executeAction()
    {
        invocation = execute();

        GetPackageAboutInfoOutput result = new GetPackageAboutInfoOutput();

  		if (invocation.getOutput("PackageAboutInfo").getValue() != null)
  		{
	        result.PackageAboutInfo = invocation.getOutput("PackageAboutInfo").getValue().toString();
  		}
  		else
  		{
	        result.PackageAboutInfo = "";
  		}

        return result;
    }
}
