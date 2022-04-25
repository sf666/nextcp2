package nextcp.upnp.modelGen.avopenhomeorg.info1.actions;

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
public class Metatext extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Metatext.class.getName());
    private ActionInvocation<?> invocation;

    public Metatext(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Metatext"), new NextcpClientInfo()), cp);

    }

    public MetatextOutput executeAction()
    {
        invocation = execute();

        MetatextOutput result = new MetatextOutput();

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
