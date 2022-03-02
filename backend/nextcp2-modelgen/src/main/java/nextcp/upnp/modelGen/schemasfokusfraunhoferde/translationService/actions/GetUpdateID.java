package nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions;

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
public class GetUpdateID extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetUpdateID.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

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
