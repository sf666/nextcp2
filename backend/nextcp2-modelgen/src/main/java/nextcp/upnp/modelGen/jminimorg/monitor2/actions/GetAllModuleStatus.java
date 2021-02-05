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
public class GetAllModuleStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAllModuleStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetAllModuleStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAllModuleStatus"), new NextcpClientInfo()), cp);

    }

    public GetAllModuleStatusOutput executeAction()
    {
        invocation = execute();

        GetAllModuleStatusOutput result = new GetAllModuleStatusOutput();

  		if (invocation.getOutput("ModuleStatusList").getValue() != null)
  		{
	        result.ModuleStatusList = invocation.getOutput("ModuleStatusList").getValue().toString();
  		}
  		else
  		{
	        result.ModuleStatusList = "";
  		}

        return result;
    }
}
