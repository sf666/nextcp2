package nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions;

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
public class GetTransportSettings extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetTransportSettings.class.getName());
    private ActionInvocation<?> invocation;

    public GetTransportSettings(Service service, GetTransportSettingsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetTransportSettings"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public GetTransportSettingsOutput executeAction()
    {
        invocation = execute();

        GetTransportSettingsOutput result = new GetTransportSettingsOutput();

        result.PlayMode = UpnpValue.toTextOrEmpty(invocation.getOutput("PlayMode").getValue());
        result.RecQualityMode = UpnpValue.toTextOrEmpty(invocation.getOutput("RecQualityMode").getValue());

        return result;
    }
}
