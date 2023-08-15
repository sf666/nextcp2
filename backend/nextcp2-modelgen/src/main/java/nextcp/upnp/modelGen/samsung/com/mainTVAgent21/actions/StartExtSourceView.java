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
public class StartExtSourceView extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StartExtSourceView.class.getName());
    private ActionInvocation<?> invocation;

    public StartExtSourceView(Service service, StartExtSourceViewInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StartExtSourceView"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Source", input.Source);
        getActionInvocation().setInput("ID", new UnsignedIntegerFourBytes(input.ID));
        getActionInvocation().setInput("ForcedFlag", input.ForcedFlag);
        getActionInvocation().setInput("DRMType", input.DRMType);
    }

    public StartExtSourceViewOutput executeAction()
    {
        invocation = execute();

        StartExtSourceViewOutput result = new StartExtSourceViewOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("ExtSourceViewURL").getValue() != null)
  		{
	        result.ExtSourceViewURL = invocation.getOutput("ExtSourceViewURL").getValue().toString();
  		}
  		else
  		{
	        result.ExtSourceViewURL = "";
  		}

        return result;
    }
}
