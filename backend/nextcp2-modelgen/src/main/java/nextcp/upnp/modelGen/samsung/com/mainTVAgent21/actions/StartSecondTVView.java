package nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions;

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
public class StartSecondTVView extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StartSecondTVView.class.getName());
    private ActionInvocation<?> invocation;

    public StartSecondTVView(Service service, StartSecondTVViewInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StartSecondTVView"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("AntennaMode", UpnpValue.forInput(getActionInvocation(), "AntennaMode", input.AntennaMode));
        getActionInvocation().setInput("Channel", UpnpValue.forInput(getActionInvocation(), "Channel", input.Channel));
        getActionInvocation().setInput("ChannelListType", UpnpValue.forInput(getActionInvocation(), "ChannelListType", input.ChannelListType));
        getActionInvocation().setInput("DRMType", UpnpValue.forInput(getActionInvocation(), "DRMType", input.DRMType));
        getActionInvocation().setInput("ForcedFlag", UpnpValue.forInput(getActionInvocation(), "ForcedFlag", input.ForcedFlag));
        getActionInvocation().setInput("SatelliteID", UpnpValue.forInput(getActionInvocation(), "SatelliteID", input.SatelliteID));
    }

    public StartSecondTVViewOutput executeAction()
    {
        invocation = execute();

        StartSecondTVViewOutput result = new StartSecondTVViewOutput();

        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());
        result.SecondTVURL = UpnpValue.toTextOrEmpty(invocation.getOutput("SecondTVURL").getValue());

        return result;
    }
}
