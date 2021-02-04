package nextcp.upnp.modelGen.avopenhomeorg.pins.actions;

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
public class SetAccount extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetAccount.class.getName());
    private ActionInvocation<?> invocation;

    public SetAccount(Service service, SetAccountInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetAccount"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Index", new UnsignedIntegerFourBytes(input.Index));
        getActionInvocation().setInput("Mode", input.Mode);
        getActionInvocation().setInput("Type", input.Type);
        getActionInvocation().setInput("Uri", input.Uri);
        getActionInvocation().setInput("Title", input.Title);
        getActionInvocation().setInput("Description", input.Description);
        getActionInvocation().setInput("ArtworkUri", input.ArtworkUri);
        getActionInvocation().setInput("Shuffle", input.Shuffle);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
