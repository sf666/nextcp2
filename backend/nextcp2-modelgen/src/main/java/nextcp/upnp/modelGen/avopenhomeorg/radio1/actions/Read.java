package nextcp.upnp.modelGen.avopenhomeorg.radio1.actions;

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
