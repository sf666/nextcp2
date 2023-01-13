package nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions;

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
public class GetUpdateID extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetUpdateID.class.getName());
    private ActionInvocation<?> invocation;

    public GetUpdateID(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetUpdateID"), new NextcpClientInfo()), cp);

    }

    public GetUpdateIDOutput executeAction()
    {
        invocation = execute();

        GetUpdateIDOutput result = new GetUpdateIDOutput();

  		if (invocation.getOutput("ID").getValue() != null)
  		{
	        result.ID = invocation.getOutput("ID").getValue().toString();
  		}
  		else
  		{
	        result.ID = "";
  		}

        return result;
    }
}
