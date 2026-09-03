package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class GetDefaultRadioSupport extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetDefaultRadioSupport.class.getName());
    private ActionInvocation<?> invocation;

    public GetDefaultRadioSupport(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetDefaultRadioSupport"), new NextcpClientInfo()), cp);
		
    }

    public GetDefaultRadioSupportOutput executeAction()
    {
        invocation = execute();

        GetDefaultRadioSupportOutput result = new GetDefaultRadioSupportOutput();

        result.Value = UpnpValue.toBoolean(invocation.getOutput("Value").getValue());

        return result;
    }
}
