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
public class SetMainTVSource extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetMainTVSource.class.getName());
    private ActionInvocation<?> invocation;

    public SetMainTVSource(Service service, SetMainTVSourceInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetMainTVSource"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Source", input.Source);
        getActionInvocation().setInput("ID", new UnsignedIntegerFourBytes(input.ID));
        getActionInvocation().setInput("UiID", new UnsignedIntegerFourBytes(input.UiID));
    }

    public SetMainTVSourceOutput executeAction()
    {
        invocation = execute();

        SetMainTVSourceOutput result = new SetMainTVSourceOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}

        return result;
    }
}
