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
public class GetTranslation extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetTranslation.class.getName());
    private ActionInvocation<?> invocation;

    public GetTranslation(Service service, GetTranslationInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetTranslation"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Text", input.Text);
        getActionInvocation().setInput("Language", input.Language);
    }

    public GetTranslationOutput executeAction()
    {
        invocation = execute();

        GetTranslationOutput result = new GetTranslationOutput();

  		if (invocation.getOutput("Translation").getValue() != null)
  		{
	        result.Translation = invocation.getOutput("Translation").getValue().toString();
  		}
  		else
  		{
	        result.Translation = "";
  		}

        return result;
    }
}
