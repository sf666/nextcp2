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
public class GetVolumeDB extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetVolumeDB.class.getName());
    private ActionInvocation<?> invocation;

    public GetVolumeDB(Service service, GetVolumeDBInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetVolumeDB"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Channel", input.Channel);
    }

    public GetVolumeDBOutput executeAction()
    {
        invocation = execute();

        GetVolumeDBOutput result = new GetVolumeDBOutput();

        result.CurrentVolume = Integer.valueOf(invocation.getOutput("CurrentVolume").getValue().toString());

        return result;
    }
}
