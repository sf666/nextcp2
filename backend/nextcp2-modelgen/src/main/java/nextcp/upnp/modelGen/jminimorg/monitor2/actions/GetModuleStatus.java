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
public class GetModuleStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetModuleStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetModuleStatus(Service service, GetModuleStatusInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetModuleStatus"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ModuleName", input.ModuleName);
    }

    public GetModuleStatusOutput executeAction()
    {
        invocation = execute();

        GetModuleStatusOutput result = new GetModuleStatusOutput();

  		if (invocation.getOutput("ModuleStatus").getValue() != null)
  		{
	        result.ModuleStatus = invocation.getOutput("ModuleStatus").getValue().toString();
  		}
  		else
  		{
	        result.ModuleStatus = "";
  		}

        return result;
    }
}
