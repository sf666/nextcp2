package nextcp.upnp.modelGen.avopenhomeorg.product.actions;

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
 */
public class Model extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Model.class.getName());
    private ActionInvocation<?> invocation;

    public Model(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Model"), new NextcpClientInfo()), cp);

    }

    public ModelOutput executeAction()
    {
        invocation = execute();

        ModelOutput result = new ModelOutput();

  		if (invocation.getOutput("Name").getValue() != null)
  		{
	        result.Name = invocation.getOutput("Name").getValue().toString();
  		}
  		else
  		{
	        result.Name = "";
  		}
  		if (invocation.getOutput("Info").getValue() != null)
  		{
	        result.Info = invocation.getOutput("Info").getValue().toString();
  		}
  		else
  		{
	        result.Info = "";
  		}
  		if (invocation.getOutput("Url").getValue() != null)
  		{
	        result.Url = invocation.getOutput("Url").getValue().toString();
  		}
  		else
  		{
	        result.Url = "";
  		}
  		if (invocation.getOutput("ImageUri").getValue() != null)
  		{
	        result.ImageUri = invocation.getOutput("ImageUri").getValue().toString();
  		}
  		else
  		{
	        result.ImageUri = "";
  		}

        return result;
    }
}
