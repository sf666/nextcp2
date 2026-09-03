package nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions;

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
public class GetPositionInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetPositionInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetPositionInfo(Service service, GetPositionInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetPositionInfo"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public GetPositionInfoOutput executeAction()
    {
        invocation = execute();

        GetPositionInfoOutput result = new GetPositionInfoOutput();

        result.AbsCount = UpnpValue.toInteger(invocation.getOutput("AbsCount").getValue());
        result.AbsTime = UpnpValue.toTextOrEmpty(invocation.getOutput("AbsTime").getValue());
        result.RelCount = UpnpValue.toInteger(invocation.getOutput("RelCount").getValue());
        result.RelTime = UpnpValue.toTextOrEmpty(invocation.getOutput("RelTime").getValue());
        result.Track = UpnpValue.toLong(invocation.getOutput("Track").getValue());
        result.TrackDuration = UpnpValue.toTextOrEmpty(invocation.getOutput("TrackDuration").getValue());
        result.TrackMetaData = UpnpValue.toTextOrEmpty(invocation.getOutput("TrackMetaData").getValue());
        result.TrackURI = UpnpValue.toTextOrEmpty(invocation.getOutput("TrackURI").getValue());

        return result;
    }
}
