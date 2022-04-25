package nextcp.upnp.modelGen.avopenhomeorg.transport1.actions;

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
public class Repeat extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Repeat.class.getName());
    private ActionInvocation<?> invocation;

    public Repeat(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Repeat"), new NextcpClientInfo()), cp);

    }

    public RepeatOutput executeAction()
    {
        invocation = execute();

        RepeatOutput result = new RepeatOutput();

        BooleanDatatype data_Repeat = new BooleanDatatype();
        result.Repeat = data_Repeat.valueOf(invocation.getOutput("Repeat").getValue().toString());

        return result;
    }
}
