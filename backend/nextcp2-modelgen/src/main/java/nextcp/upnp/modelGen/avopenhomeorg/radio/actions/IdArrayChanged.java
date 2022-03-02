package nextcp.upnp.modelGen.avopenhomeorg.radio.actions;

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
public class IdArrayChanged extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IdArrayChanged.class.getName());
    private ActionInvocation<?> invocation;

    public IdArrayChanged(Service service, IdArrayChangedInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("IdArrayChanged"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Token", new UnsignedIntegerFourBytes(input.Token));
    }

    public IdArrayChangedOutput executeAction()
    {
        invocation = execute();

        IdArrayChangedOutput result = new IdArrayChangedOutput();

        BooleanDatatype data_Value = new BooleanDatatype();
        result.Value = data_Value.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
