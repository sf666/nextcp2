package nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions;

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
public class SetNetwork extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetNetwork.class.getName());
    private ActionInvocation<?> invocation;

    public SetNetwork(Service service, SetNetworkInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetNetwork"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("AuthAlgo", UpnpValue.forInput(getActionInvocation(), "AuthAlgo", input.AuthAlgo));
        getActionInvocation().setInput("CipherAlgo", UpnpValue.forInput(getActionInvocation(), "CipherAlgo", input.CipherAlgo));
        getActionInvocation().setInput("Key", UpnpValue.forInput(getActionInvocation(), "Key", input.Key));
        getActionInvocation().setInput("SSID", UpnpValue.forInput(getActionInvocation(), "SSID", input.SSID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
