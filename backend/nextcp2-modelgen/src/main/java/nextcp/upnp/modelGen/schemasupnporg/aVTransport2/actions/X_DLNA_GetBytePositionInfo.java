package nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions;

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
public class X_DLNA_GetBytePositionInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_DLNA_GetBytePositionInfo.class.getName());
    private ActionInvocation<?> invocation;

    public X_DLNA_GetBytePositionInfo(Service service, X_DLNA_GetBytePositionInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_DLNA_GetBytePositionInfo"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public X_DLNA_GetBytePositionInfoOutput executeAction()
    {
        invocation = execute();

        X_DLNA_GetBytePositionInfoOutput result = new X_DLNA_GetBytePositionInfoOutput();

        result.AbsByte = UpnpValue.toTextOrEmpty(invocation.getOutput("AbsByte").getValue());
        result.RelByte = UpnpValue.toTextOrEmpty(invocation.getOutput("RelByte").getValue());
        result.TrackSize = UpnpValue.toTextOrEmpty(invocation.getOutput("TrackSize").getValue());

        return result;
    }
}
