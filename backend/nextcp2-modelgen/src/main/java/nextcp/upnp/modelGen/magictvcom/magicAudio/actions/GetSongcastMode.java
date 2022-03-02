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
public class GetSongcastMode extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSongcastMode.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public GetSongcastMode(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSongcastMode"), new NextcpClientInfo()), cp);

    }

    public GetSongcastModeOutput executeAction()
    {
        invocation = execute();

        GetSongcastModeOutput result = new GetSongcastModeOutput();

        BooleanDatatype data_Value = new BooleanDatatype();
        result.Value = data_Value.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
