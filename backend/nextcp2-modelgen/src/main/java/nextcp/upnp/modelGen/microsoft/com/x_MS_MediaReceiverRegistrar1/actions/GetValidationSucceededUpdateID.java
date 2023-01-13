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
public class GetValidationSucceededUpdateID extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetValidationSucceededUpdateID.class.getName());
    private ActionInvocation<?> invocation;

    public GetValidationSucceededUpdateID(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetValidationSucceededUpdateID"), new NextcpClientInfo()), cp);

    }

    public GetValidationSucceededUpdateIDOutput executeAction()
    {
        invocation = execute();

        GetValidationSucceededUpdateIDOutput result = new GetValidationSucceededUpdateIDOutput();

        result.ValidationSucceededUpdateID = ((UnsignedIntegerFourBytes) invocation.getOutput("ValidationSucceededUpdateID").getValue()).getValue();

        return result;
    }
}
