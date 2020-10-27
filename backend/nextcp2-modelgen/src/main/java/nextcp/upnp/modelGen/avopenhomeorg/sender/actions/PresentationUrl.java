package nextcp.upnp.modelGen.avopenhomeorg.sender.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class PresentationUrl extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(PresentationUrl.class.getName());
    private ActionInvocation<?> invocation;

    public PresentationUrl(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("PresentationUrl")), cp);

    }

    public PresentationUrlOutput executeAction()
    {
        invocation = execute();

        PresentationUrlOutput result = new PresentationUrlOutput();

  		if (invocation.getOutput("Value").getValue() != null)
  		{
	        result.Value = invocation.getOutput("Value").getValue().toString();
  		}
  		else
  		{
	        result.Value = "";
  		}

        return result;
    }
}
