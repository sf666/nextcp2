package nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

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
public class GetMediaInfo_Ext extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMediaInfo_Ext.class.getName());
    private ActionInvocation<?> invocation;

    public GetMediaInfo_Ext(Service service, GetMediaInfo_ExtInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMediaInfo_Ext"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetMediaInfo_ExtOutput executeAction()
    {
        invocation = execute();

        GetMediaInfo_ExtOutput result = new GetMediaInfo_ExtOutput();

  		if (invocation.getOutput("CurrentType").getValue() != null)
  		{
	        result.CurrentType = invocation.getOutput("CurrentType").getValue().toString();
  		}
  		else
  		{
	        result.CurrentType = "";
  		}
        result.NrTracks = ((UnsignedIntegerFourBytes) invocation.getOutput("NrTracks").getValue()).getValue();
  		if (invocation.getOutput("MediaDuration").getValue() != null)
  		{
	        result.MediaDuration = invocation.getOutput("MediaDuration").getValue().toString();
  		}
  		else
  		{
	        result.MediaDuration = "";
  		}
  		if (invocation.getOutput("CurrentURI").getValue() != null)
  		{
	        result.CurrentURI = invocation.getOutput("CurrentURI").getValue().toString();
  		}
  		else
  		{
	        result.CurrentURI = "";
  		}
  		if (invocation.getOutput("CurrentURIMetaData").getValue() != null)
  		{
	        result.CurrentURIMetaData = invocation.getOutput("CurrentURIMetaData").getValue().toString();
  		}
  		else
  		{
	        result.CurrentURIMetaData = "";
  		}
  		if (invocation.getOutput("NextURI").getValue() != null)
  		{
	        result.NextURI = invocation.getOutput("NextURI").getValue().toString();
  		}
  		else
  		{
	        result.NextURI = "";
  		}
  		if (invocation.getOutput("NextURIMetaData").getValue() != null)
  		{
	        result.NextURIMetaData = invocation.getOutput("NextURIMetaData").getValue().toString();
  		}
  		else
  		{
	        result.NextURIMetaData = "";
  		}
  		if (invocation.getOutput("PlayMedium").getValue() != null)
  		{
	        result.PlayMedium = invocation.getOutput("PlayMedium").getValue().toString();
  		}
  		else
  		{
	        result.PlayMedium = "";
  		}
  		if (invocation.getOutput("RecordMedium").getValue() != null)
  		{
	        result.RecordMedium = invocation.getOutput("RecordMedium").getValue().toString();
  		}
  		else
  		{
	        result.RecordMedium = "";
  		}
  		if (invocation.getOutput("WriteStatus").getValue() != null)
  		{
	        result.WriteStatus = invocation.getOutput("WriteStatus").getValue().toString();
  		}
  		else
  		{
	        result.WriteStatus = "";
  		}

        return result;
    }
}
