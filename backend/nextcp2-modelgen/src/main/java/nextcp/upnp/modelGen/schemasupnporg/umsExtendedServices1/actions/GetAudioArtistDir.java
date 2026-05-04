package nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions;

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
public class GetAudioArtistDir extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAudioArtistDir.class.getName());
    private ActionInvocation<?> invocation;

    public GetAudioArtistDir(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAudioArtistDir"), new NextcpClientInfo()), cp);
		
    }

    public GetAudioArtistDirOutput executeAction()
    {
        invocation = execute();

        GetAudioArtistDirOutput result = new GetAudioArtistDirOutput();

  		if (invocation.getOutput("ObjectID").getValue() != null)
  		{
	        result.ObjectID = invocation.getOutput("ObjectID").getValue().toString();
  		}
  		else
  		{
	        result.ObjectID = "";
  		}

        return result;
    }
}
