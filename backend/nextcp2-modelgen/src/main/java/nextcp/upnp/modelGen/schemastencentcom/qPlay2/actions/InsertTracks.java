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
import nextcp.upnp.UpnpValue;

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
		
        getActionInvocation().setInput("QueueID", UpnpValue.forInput(getActionInvocation(), "QueueID", input.QueueID));
        getActionInvocation().setInput("StartingIndex", UpnpValue.forInput(getActionInvocation(), "StartingIndex", input.StartingIndex));
        getActionInvocation().setInput("TracksMetaData", UpnpValue.forInput(getActionInvocation(), "TracksMetaData", input.TracksMetaData));
    }

    public InsertTracksOutput executeAction()
    {
        invocation = execute();

        InsertTracksOutput result = new InsertTracksOutput();

        result.NumberOfSuccess = UpnpValue.toTextOrEmpty(invocation.getOutput("NumberOfSuccess").getValue());

        return result;
    }
}
