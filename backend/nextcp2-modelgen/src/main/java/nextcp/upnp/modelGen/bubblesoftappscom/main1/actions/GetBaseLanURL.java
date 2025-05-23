package nextcp.upnp.modelGen.bubblesoftappscom.main1.actions;

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
public class GetBaseLanURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetBaseLanURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetBaseLanURL(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetBaseLanURL"), new NextcpClientInfo()), cp);

    }

    public GetBaseLanURLOutput executeAction()
    {
        invocation = execute();

        GetBaseLanURLOutput result = new GetBaseLanURLOutput();

  		if (invocation.getOutput("BaseLanURL").getValue() != null)
  		{
	        result.BaseLanURL = invocation.getOutput("BaseLanURL").getValue().toString();
  		}
  		else
  		{
	        result.BaseLanURL = "";
  		}

        return result;
    }
}
