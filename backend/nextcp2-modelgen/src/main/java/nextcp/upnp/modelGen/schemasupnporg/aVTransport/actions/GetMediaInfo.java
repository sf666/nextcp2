package nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions;

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
 */
public class GetMediaInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMediaInfo.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public GetMediaInfo(Service service, GetMediaInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMediaInfo"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetMediaInfoOutput executeAction()
    {
        invocation = execute();

        GetMediaInfoOutput result = new GetMediaInfoOutput();

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
