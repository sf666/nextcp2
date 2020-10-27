package nextcp.upnp.modelGen.avopenhomeorg.radio.actions;

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
public class IdArray extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IdArray.class.getName());
    private ActionInvocation<?> invocation;

    public IdArray(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("IdArray")), cp);

    }

    public IdArrayOutput executeAction()
    {
        invocation = execute();

        IdArrayOutput result = new IdArrayOutput();

        result.Token = ((UnsignedIntegerFourBytes) invocation.getOutput("Token").getValue()).getValue();
        result.Array = (byte[]) invocation.getOutput("Array").getValue();

        return result;
    }
}
