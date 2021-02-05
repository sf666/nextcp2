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
public class InstallPackage extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(InstallPackage.class.getName());
    private ActionInvocation<?> invocation;

    public InstallPackage(Service service, InstallPackageInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("InstallPackage"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("PackageName", input.PackageName);
        getActionInvocation().setInput("PackageURLPath", input.PackageURLPath);
        getActionInvocation().setInput("ModuleURLPath", input.ModuleURLPath);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
