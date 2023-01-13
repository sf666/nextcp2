package nextcp.upnp.modelGen.avopenhomeorg.info1.actions;

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
