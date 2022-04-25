package nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions;

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
public class GetSequenceNumber extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSequenceNumber.class.getName());
    private ActionInvocation<?> invocation;

    public GetSequenceNumber(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSequenceNumber"), new NextcpClientInfo()), cp);

    }

    public GetSequenceNumberOutput executeAction()
    {
        invocation = execute();

        GetSequenceNumberOutput result = new GetSequenceNumberOutput();

        result.SequenceNumber = ((UnsignedIntegerFourBytes) invocation.getOutput("SequenceNumber").getValue()).getValue();

        return result;
    }
}