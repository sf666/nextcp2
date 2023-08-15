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
public class StartSecondTVView extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StartSecondTVView.class.getName());
    private ActionInvocation<?> invocation;

    public StartSecondTVView(Service service, StartSecondTVViewInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StartSecondTVView"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("AntennaMode", new UnsignedIntegerFourBytes(input.AntennaMode));
        getActionInvocation().setInput("ChannelListType", input.ChannelListType);
        getActionInvocation().setInput("SatelliteID", new UnsignedIntegerFourBytes(input.SatelliteID));
        getActionInvocation().setInput("Channel", input.Channel);
        getActionInvocation().setInput("ForcedFlag", input.ForcedFlag);
        getActionInvocation().setInput("DRMType", input.DRMType);
    }

    public StartSecondTVViewOutput executeAction()
    {
        invocation = execute();

        StartSecondTVViewOutput result = new StartSecondTVViewOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("SecondTVURL").getValue() != null)
  		{
	        result.SecondTVURL = invocation.getOutput("SecondTVURL").getValue().toString();
  		}
  		else
  		{
	        result.SecondTVURL = "";
  		}

        return result;
    }
}
