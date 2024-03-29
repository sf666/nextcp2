package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

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
public class GetMute extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMute.class.getName());
    private ActionInvocation<?> invocation;

    public GetMute(Service service, GetMuteInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMute"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Channel", input.Channel);
    }

    public GetMuteOutput executeAction()
    {
        invocation = execute();

        GetMuteOutput result = new GetMuteOutput();

        BooleanDatatype data_CurrentMute = new BooleanDatatype();
        result.CurrentMute = data_CurrentMute.valueOf(invocation.getOutput("CurrentMute").getValue().toString());

        return result;
    }
}
