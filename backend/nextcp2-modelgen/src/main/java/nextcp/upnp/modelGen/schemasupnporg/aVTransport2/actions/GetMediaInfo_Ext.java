package nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions;

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
public class GetMediaInfo_Ext extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMediaInfo_Ext.class.getName());
    private ActionInvocation<?> invocation;

    public GetMediaInfo_Ext(Service service, GetMediaInfo_ExtInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMediaInfo_Ext"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public GetMediaInfo_ExtOutput executeAction()
    {
        invocation = execute();

        GetMediaInfo_ExtOutput result = new GetMediaInfo_ExtOutput();

        result.CurrentType = UpnpValue.toTextOrEmpty(invocation.getOutput("CurrentType").getValue());
        result.CurrentURI = UpnpValue.toTextOrEmpty(invocation.getOutput("CurrentURI").getValue());
        result.CurrentURIMetaData = UpnpValue.toTextOrEmpty(invocation.getOutput("CurrentURIMetaData").getValue());
        result.MediaDuration = UpnpValue.toTextOrEmpty(invocation.getOutput("MediaDuration").getValue());
        result.NextURI = UpnpValue.toTextOrEmpty(invocation.getOutput("NextURI").getValue());
        result.NextURIMetaData = UpnpValue.toTextOrEmpty(invocation.getOutput("NextURIMetaData").getValue());
        result.NrTracks = UpnpValue.toLong(invocation.getOutput("NrTracks").getValue());
        result.PlayMedium = UpnpValue.toTextOrEmpty(invocation.getOutput("PlayMedium").getValue());
        result.RecordMedium = UpnpValue.toTextOrEmpty(invocation.getOutput("RecordMedium").getValue());
        result.WriteStatus = UpnpValue.toTextOrEmpty(invocation.getOutput("WriteStatus").getValue());

        return result;
    }
}
