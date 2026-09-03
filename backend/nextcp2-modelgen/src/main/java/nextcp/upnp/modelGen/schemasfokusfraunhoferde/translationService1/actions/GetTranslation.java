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
import nextcp.upnp.UpnpValue;

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
		
        getActionInvocation().setInput("Language", UpnpValue.forInput(getActionInvocation(), "Language", input.Language));
        getActionInvocation().setInput("Text", UpnpValue.forInput(getActionInvocation(), "Text", input.Text));
    }

    public GetTranslationOutput executeAction()
    {
        invocation = execute();

        GetTranslationOutput result = new GetTranslationOutput();

        result.Translation = UpnpValue.toTextOrEmpty(invocation.getOutput("Translation").getValue());

        return result;
    }
}
