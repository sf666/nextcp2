package nextcp.upnp.modelGen.avopenhomeorg.transport1.actions;

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
public class ModeInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ModeInfo.class.getName());
    private ActionInvocation<?> invocation;

    public ModeInfo(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ModeInfo"), new NextcpClientInfo()), cp);

    }

    public ModeInfoOutput executeAction()
    {
        invocation = execute();

        ModeInfoOutput result = new ModeInfoOutput();

        BooleanDatatype data_CanSkipNext = new BooleanDatatype();
        result.CanSkipNext = data_CanSkipNext.valueOf(invocation.getOutput("CanSkipNext").getValue().toString());
        BooleanDatatype data_CanSkipPrevious = new BooleanDatatype();
        result.CanSkipPrevious = data_CanSkipPrevious.valueOf(invocation.getOutput("CanSkipPrevious").getValue().toString());
        BooleanDatatype data_CanRepeat = new BooleanDatatype();
        result.CanRepeat = data_CanRepeat.valueOf(invocation.getOutput("CanRepeat").getValue().toString());
        BooleanDatatype data_CanShuffle = new BooleanDatatype();
        result.CanShuffle = data_CanShuffle.valueOf(invocation.getOutput("CanShuffle").getValue().toString());

        return result;
    }
}
