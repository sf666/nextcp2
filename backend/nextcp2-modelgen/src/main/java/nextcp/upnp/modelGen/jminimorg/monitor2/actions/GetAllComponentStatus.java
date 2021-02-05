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
public class GetAllComponentStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAllComponentStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetAllComponentStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAllComponentStatus"), new NextcpClientInfo()), cp);

    }

    public GetAllComponentStatusOutput executeAction()
    {
        invocation = execute();

        GetAllComponentStatusOutput result = new GetAllComponentStatusOutput();

  		if (invocation.getOutput("ComponentStatusList").getValue() != null)
  		{
	        result.ComponentStatusList = invocation.getOutput("ComponentStatusList").getValue().toString();
  		}
  		else
  		{
	        result.ComponentStatusList = "";
  		}

        return result;
    }
}
