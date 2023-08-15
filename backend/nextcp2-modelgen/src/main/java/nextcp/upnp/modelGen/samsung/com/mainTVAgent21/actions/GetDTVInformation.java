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
public class GetDTVInformation extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetDTVInformation.class.getName());
    private ActionInvocation<?> invocation;

    public GetDTVInformation(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetDTVInformation"), new NextcpClientInfo()), cp);

    }

    public GetDTVInformationOutput executeAction()
    {
        invocation = execute();

        GetDTVInformationOutput result = new GetDTVInformationOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("DTVInformation").getValue() != null)
  		{
	        result.DTVInformation = invocation.getOutput("DTVInformation").getValue().toString();
  		}
  		else
  		{
	        result.DTVInformation = "";
  		}

        return result;
    }
}
