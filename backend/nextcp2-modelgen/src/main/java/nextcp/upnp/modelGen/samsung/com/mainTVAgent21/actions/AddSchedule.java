package nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions;

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
public class AddSchedule extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(AddSchedule.class.getName());
    private ActionInvocation<?> invocation;

    public AddSchedule(Service service, AddScheduleInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("AddSchedule"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("RemindInfo", UpnpValue.forInput(getActionInvocation(), "RemindInfo", input.RemindInfo));
        getActionInvocation().setInput("ReservationType", UpnpValue.forInput(getActionInvocation(), "ReservationType", input.ReservationType));
    }

    public AddScheduleOutput executeAction()
    {
        invocation = execute();

        AddScheduleOutput result = new AddScheduleOutput();

        result.ConflictRemindInfo = UpnpValue.toTextOrEmpty(invocation.getOutput("ConflictRemindInfo").getValue());
        result.ConflictRemindInfoURL = UpnpValue.toTextOrEmpty(invocation.getOutput("ConflictRemindInfoURL").getValue());
        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
