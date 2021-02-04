package nextcp.upnp.modelGen.avopenhomeorg.info.actions;

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
public class Track extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Track.class.getName());
    private ActionInvocation<?> invocation;

    public Track(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Track"), new NextcpClientInfo()), cp);

    }

    public TrackOutput executeAction()
    {
        invocation = execute();

        TrackOutput result = new TrackOutput();

  		if (invocation.getOutput("Uri").getValue() != null)
  		{
	        result.Uri = invocation.getOutput("Uri").getValue().toString();
  		}
  		else
  		{
	        result.Uri = "";
  		}
  		if (invocation.getOutput("Metadata").getValue() != null)
  		{
	        result.Metadata = invocation.getOutput("Metadata").getValue().toString();
  		}
  		else
  		{
	        result.Metadata = "";
  		}

        return result;
    }
}
