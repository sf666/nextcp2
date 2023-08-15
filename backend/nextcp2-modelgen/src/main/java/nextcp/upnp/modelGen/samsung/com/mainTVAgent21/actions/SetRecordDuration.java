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

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class SetRecordDuration extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetRecordDuration.class.getName());
    private ActionInvocation<?> invocation;

    public SetRecordDuration(Service service, SetRecordDurationInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetRecordDuration"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Channel", input.Channel);
        getActionInvocation().setInput("RecordDuration", new UnsignedIntegerFourBytes(input.RecordDuration));
    }

    public SetRecordDurationOutput executeAction()
    {
        invocation = execute();

        SetRecordDurationOutput result = new SetRecordDurationOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}

        return result;
    }
}
