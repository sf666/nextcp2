package nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions;

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
public class StartCloneView extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StartCloneView.class.getName());
    private ActionInvocation<?> invocation;

    public StartCloneView(Service service, StartCloneViewInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StartCloneView"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ForcedFlag", input.ForcedFlag);
        getActionInvocation().setInput("DRMType", input.DRMType);
    }

    public StartCloneViewOutput executeAction()
    {
        invocation = execute();

        StartCloneViewOutput result = new StartCloneViewOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("CloneViewURL").getValue() != null)
  		{
	        result.CloneViewURL = invocation.getOutput("CloneViewURL").getValue().toString();
  		}
  		else
  		{
	        result.CloneViewURL = "";
  		}

        return result;
    }
}
