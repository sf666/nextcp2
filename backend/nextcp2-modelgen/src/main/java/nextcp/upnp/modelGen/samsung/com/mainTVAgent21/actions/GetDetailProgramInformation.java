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
public class GetDetailProgramInformation extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetDetailProgramInformation.class.getName());
    private ActionInvocation<?> invocation;

    public GetDetailProgramInformation(Service service, GetDetailProgramInformationInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetDetailProgramInformation"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("AntennaMode", new UnsignedIntegerFourBytes(input.AntennaMode));
        getActionInvocation().setInput("Channel", input.Channel);
        getActionInvocation().setInput("StartTime", input.StartTime);
    }

    public GetDetailProgramInformationOutput executeAction()
    {
        invocation = execute();

        GetDetailProgramInformationOutput result = new GetDetailProgramInformationOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("DetailProgramInformation").getValue() != null)
  		{
	        result.DetailProgramInformation = invocation.getOutput("DetailProgramInformation").getValue().toString();
  		}
  		else
  		{
	        result.DetailProgramInformation = "";
  		}

        return result;
    }
}
