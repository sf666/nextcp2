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
public class GetTranslationList extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetTranslationList.class.getName());
    private ActionInvocation<?> invocation;

    public GetTranslationList(Service service, GetTranslationListInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetTranslationList"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Language", input.Language);
    }

    public GetTranslationListOutput executeAction()
    {
        invocation = execute();

        GetTranslationListOutput result = new GetTranslationListOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}

        return result;
    }
}
