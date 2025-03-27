package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class GetAppDisplayMessage extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAppDisplayMessage.class.getName());
    private ActionInvocation<?> invocation;

    public GetAppDisplayMessage(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAppDisplayMessage"), new NextcpClientInfo()), cp);

    }

    public GetAppDisplayMessageOutput executeAction()
    {
        invocation = execute();

        GetAppDisplayMessageOutput result = new GetAppDisplayMessageOutput();

        result.Id = ((UnsignedIntegerFourBytes) invocation.getOutput("Id").getValue()).getValue();
  		if (invocation.getOutput("String").getValue() != null)
  		{
	        result.String = invocation.getOutput("String").getValue().toString();
  		}
  		else
  		{
	        result.String = "";
  		}
        result.Tag = ((UnsignedIntegerFourBytes) invocation.getOutput("Tag").getValue()).getValue();

        return result;
    }
}
