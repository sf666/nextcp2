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
public class InsertTracks extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(InsertTracks.class.getName());
    private ActionInvocation<?> invocation;

    public InsertTracks(Service service, InsertTracksInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("InsertTracks"), new NextcpClientInfo()), cp);
		
        if (input.QueueID != null) {
	        getActionInvocation().setInput("QueueID", input.QueueID);
		} else {
    	    getActionInvocation().setInput("QueueID", null);
		}
        if (input.StartingIndex != null) {
	        getActionInvocation().setInput("StartingIndex", input.StartingIndex);
		} else {
    	    getActionInvocation().setInput("StartingIndex", null);
		}
        if (input.TracksMetaData != null) {
	        getActionInvocation().setInput("TracksMetaData", input.TracksMetaData);
		} else {
    	    getActionInvocation().setInput("TracksMetaData", null);
		}
    }

    public InsertTracksOutput executeAction()
    {
        invocation = execute();

        InsertTracksOutput result = new InsertTracksOutput();

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
