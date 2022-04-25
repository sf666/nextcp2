package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

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
public class GetVolumeDBRange extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetVolumeDBRange.class.getName());
    private ActionInvocation<?> invocation;

    public GetVolumeDBRange(Service service, GetVolumeDBRangeInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetVolumeDBRange"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Channel", input.Channel);
    }

    public GetVolumeDBRangeOutput executeAction()
    {
        invocation = execute();

        GetVolumeDBRangeOutput result = new GetVolumeDBRangeOutput();

        result.MinValue = Integer.valueOf(invocation.getOutput("MinValue").getValue().toString());
        result.MaxValue = Integer.valueOf(invocation.getOutput("MaxValue").getValue().toString());

        return result;
    }
}
