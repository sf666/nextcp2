package nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions;

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
public class Read extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Read.class.getName());
    private ActionInvocation<?> invocation;

    public Read(Service service, ReadInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Read"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Id", new UnsignedIntegerFourBytes(input.Id));
    }

    public ReadOutput executeAction()
    {
        invocation = execute();

        ReadOutput result = new ReadOutput();

  		if (invocation.getOutput("Uri").getValue() != null)
  		{
	        result.Uri = invocation.getOutput("Uri").getValue().toString();
  		}
  		else
  		{
	        result.Uri = "";
  		}
  		if (invocation.getOutput("Metadata").getValue() != null)
  		{
	        result.Metadata = invocation.getOutput("Metadata").getValue().toString();
  		}
  		else
  		{
	        result.Metadata = "";
  		}

        return result;
    }
}
