package nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions;

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
public class CreateObject extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(CreateObject.class.getName());
    private ActionInvocation<?> invocation;

    public CreateObject(Service service, CreateObjectInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("CreateObject"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ContainerID", input.ContainerID);
        getActionInvocation().setInput("Elements", input.Elements);
    }

    public CreateObjectOutput executeAction()
    {
        invocation = execute();

        CreateObjectOutput result = new CreateObjectOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("ObjectID").getValue() != null)
  		{
	        result.ObjectID = invocation.getOutput("ObjectID").getValue().toString();
  		}
  		else
  		{
	        result.ObjectID = "";
  		}

        return result;
    }
}
