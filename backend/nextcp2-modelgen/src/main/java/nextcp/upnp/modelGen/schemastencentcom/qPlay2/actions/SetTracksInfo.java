package nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions;

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
public class SetTracksInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetTracksInfo.class.getName());
    private ActionInvocation<?> invocation;

    public SetTracksInfo(Service service, SetTracksInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetTracksInfo"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("QueueID", input.QueueID);
        getActionInvocation().setInput("StartingIndex", input.StartingIndex);
        getActionInvocation().setInput("NextIndex", input.NextIndex);
        getActionInvocation().setInput("TracksMetaData", input.TracksMetaData);
    }

    public SetTracksInfoOutput executeAction()
    {
        invocation = execute();

        SetTracksInfoOutput result = new SetTracksInfoOutput();

  		if (invocation.getOutput("NumberOfSuccess").getValue() != null)
  		{
	        result.NumberOfSuccess = invocation.getOutput("NumberOfSuccess").getValue().toString();
  		}
  		else
  		{
	        result.NumberOfSuccess = "";
  		}

        return result;
    }
}
