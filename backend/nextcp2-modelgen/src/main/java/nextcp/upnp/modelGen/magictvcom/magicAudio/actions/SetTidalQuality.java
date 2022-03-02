package nextcp.upnp.modelGen.magictvcom.magicAudio.actions;

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
public class SetTidalQuality extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetTidalQuality.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public SetTidalQuality(Service service, SetTidalQualityInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetTidalQuality"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Quality", input.Quality);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
