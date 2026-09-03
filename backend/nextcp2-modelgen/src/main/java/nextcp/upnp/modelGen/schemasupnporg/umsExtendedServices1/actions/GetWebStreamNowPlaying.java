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
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class GetWebStreamNowPlaying extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetWebStreamNowPlaying.class.getName());
    private ActionInvocation<?> invocation;

    public GetWebStreamNowPlaying(Service service, GetWebStreamNowPlayingInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetWebStreamNowPlaying"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("ObjectID", UpnpValue.forInput(getActionInvocation(), "ObjectID", input.ObjectID));
    }

    public GetWebStreamNowPlayingOutput executeAction()
    {
        invocation = execute();

        GetWebStreamNowPlayingOutput result = new GetWebStreamNowPlayingOutput();

        result.NowPlaying = UpnpValue.toTextOrEmpty(invocation.getOutput("NowPlaying").getValue());

        return result;
    }
}
