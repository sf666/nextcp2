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
