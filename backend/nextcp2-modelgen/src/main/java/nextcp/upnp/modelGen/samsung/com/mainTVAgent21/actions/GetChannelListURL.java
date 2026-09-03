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
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class GetChannelListURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetChannelListURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetChannelListURL(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetChannelListURL"), new NextcpClientInfo()), cp);
		
    }

    public GetChannelListURLOutput executeAction()
    {
        invocation = execute();

        GetChannelListURLOutput result = new GetChannelListURLOutput();

        result.ChannelListType = UpnpValue.toTextOrEmpty(invocation.getOutput("ChannelListType").getValue());
        result.ChannelListURL = UpnpValue.toTextOrEmpty(invocation.getOutput("ChannelListURL").getValue());
        result.ChannelListVersion = UpnpValue.toLong(invocation.getOutput("ChannelListVersion").getValue());
        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());
        result.SatelliteID = UpnpValue.toLong(invocation.getOutput("SatelliteID").getValue());
        result.SupportChannelList = UpnpValue.toTextOrEmpty(invocation.getOutput("SupportChannelList").getValue());

        return result;
    }
}
