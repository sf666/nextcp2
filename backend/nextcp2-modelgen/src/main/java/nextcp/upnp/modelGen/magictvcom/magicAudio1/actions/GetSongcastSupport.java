package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class GetSongcastSupport extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSongcastSupport.class.getName());
    private ActionInvocation<?> invocation;

    public GetSongcastSupport(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSongcastSupport"), new NextcpClientInfo()), cp);

    }

    public GetSongcastSupportOutput executeAction()
    {
        invocation = execute();

        GetSongcastSupportOutput result = new GetSongcastSupportOutput();

        BooleanDatatype data_Value = new BooleanDatatype();
        result.Value = data_Value.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
