package nextcp.upnp.modelGen.schemastencentcom.qPlay1.actions;

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
public class GetTracksInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetTracksInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetTracksInfo(Service service, GetTracksInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetTracksInfo"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("NumberOfTracks", UpnpValue.forInput(getActionInvocation(), "NumberOfTracks", input.NumberOfTracks));
        getActionInvocation().setInput("StartingIndex", UpnpValue.forInput(getActionInvocation(), "StartingIndex", input.StartingIndex));
    }

    public GetTracksInfoOutput executeAction()
    {
        invocation = execute();

        GetTracksInfoOutput result = new GetTracksInfoOutput();

        result.TracksMetaData = UpnpValue.toTextOrEmpty(invocation.getOutput("TracksMetaData").getValue());

        return result;
    }
}
