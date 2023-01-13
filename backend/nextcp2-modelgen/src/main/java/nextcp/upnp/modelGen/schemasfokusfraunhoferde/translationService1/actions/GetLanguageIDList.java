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
public class GetLanguageIDList extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetLanguageIDList.class.getName());
    private ActionInvocation<?> invocation;

    public GetLanguageIDList(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetLanguageIDList"), new NextcpClientInfo()), cp);

    }

    public GetLanguageIDListOutput executeAction()
    {
        invocation = execute();

        GetLanguageIDListOutput result = new GetLanguageIDListOutput();

  		if (invocation.getOutput("IDList").getValue() != null)
  		{
	        result.IDList = invocation.getOutput("IDList").getValue().toString();
  		}
  		else
  		{
	        result.IDList = "";
  		}

        return result;
    }
}
