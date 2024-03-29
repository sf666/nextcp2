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
