package nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions;

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
public class X_Get3DFormatter extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_Get3DFormatter.class.getName());
    private ActionInvocation<?> invocation;

    public X_Get3DFormatter(Service service, X_Get3DFormatterInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_Get3DFormatter"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public X_Get3DFormatterOutput executeAction()
    {
        invocation = execute();

        X_Get3DFormatterOutput result = new X_Get3DFormatterOutput();

  		if (invocation.getOutput("Current3DFormatter").getValue() != null)
  		{
	        result.Current3DFormatter = invocation.getOutput("Current3DFormatter").getValue().toString();
  		}
  		else
  		{
	        result.Current3DFormatter = "";
  		}
  		if (invocation.getOutput("Possible3DFormatter").getValue() != null)
  		{
	        result.Possible3DFormatter = invocation.getOutput("Possible3DFormatter").getValue().toString();
  		}
  		else
  		{
	        result.Possible3DFormatter = "";
  		}

        return result;
    }
}
