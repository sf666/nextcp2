package nextcp.upnp.modelGen.avopenhomeorg.product2.actions;

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
public class Product extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Product.class.getName());
    private ActionInvocation<?> invocation;

    public Product(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Product"), new NextcpClientInfo()), cp);

    }

    public ProductOutput executeAction()
    {
        invocation = execute();

        ProductOutput result = new ProductOutput();

  		if (invocation.getOutput("Room").getValue() != null)
  		{
	        result.Room = invocation.getOutput("Room").getValue().toString();
  		}
  		else
  		{
	        result.Room = "";
  		}
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
