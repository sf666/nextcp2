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
public class PlayRecordedItem extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(PlayRecordedItem.class.getName());
    private ActionInvocation<?> invocation;

    public PlayRecordedItem(Service service, PlayRecordedItemInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("PlayRecordedItem"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("UID", new UnsignedIntegerFourBytes(input.UID));
    }

    public PlayRecordedItemOutput executeAction()
    {
        invocation = execute();

        PlayRecordedItemOutput result = new PlayRecordedItemOutput();

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
