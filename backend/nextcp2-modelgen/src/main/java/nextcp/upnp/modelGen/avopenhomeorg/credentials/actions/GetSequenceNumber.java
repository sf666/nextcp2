package nextcp.upnp.modelGen.avopenhomeorg.credentials.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class GetSequenceNumber extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSequenceNumber.class.getName());
    private ActionInvocation<?> invocation;

    public GetSequenceNumber(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSequenceNumber")), cp);

    }

    public GetSequenceNumberOutput executeAction()
    {
        invocation = execute();

        GetSequenceNumberOutput result = new GetSequenceNumberOutput();

        result.SequenceNumber = ((UnsignedIntegerFourBytes) invocation.getOutput("SequenceNumber").getValue()).getValue();

        return result;
    }
}
