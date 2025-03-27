package nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions;

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
public class QPlayAuth extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(QPlayAuth.class.getName());
    private ActionInvocation<?> invocation;

    public QPlayAuth(Service service, QPlayAuthInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("QPlayAuth"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Seed", input.Seed);
    }

    public QPlayAuthOutput executeAction()
    {
        invocation = execute();

        QPlayAuthOutput result = new QPlayAuthOutput();

  		if (invocation.getOutput("Code").getValue() != null)
  		{
	        result.Code = invocation.getOutput("Code").getValue().toString();
  		}
  		else
  		{
	        result.Code = "";
  		}
  		if (invocation.getOutput("MID").getValue() != null)
  		{
	        result.MID = invocation.getOutput("MID").getValue().toString();
  		}
  		else
  		{
	        result.MID = "";
  		}
  		if (invocation.getOutput("DID").getValue() != null)
  		{
	        result.DID = invocation.getOutput("DID").getValue().toString();
  		}
  		else
  		{
	        result.DID = "";
  		}

        return result;
    }
}
