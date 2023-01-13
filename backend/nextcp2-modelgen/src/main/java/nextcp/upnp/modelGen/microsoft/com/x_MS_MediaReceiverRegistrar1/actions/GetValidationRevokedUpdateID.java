package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions;

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
public class GetValidationRevokedUpdateID extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetValidationRevokedUpdateID.class.getName());
    private ActionInvocation<?> invocation;

    public GetValidationRevokedUpdateID(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetValidationRevokedUpdateID"), new NextcpClientInfo()), cp);

    }

    public GetValidationRevokedUpdateIDOutput executeAction()
    {
        invocation = execute();

        GetValidationRevokedUpdateIDOutput result = new GetValidationRevokedUpdateIDOutput();

        result.ValidationRevokedUpdateID = ((UnsignedIntegerFourBytes) invocation.getOutput("ValidationRevokedUpdateID").getValue()).getValue();

        return result;
    }
}
