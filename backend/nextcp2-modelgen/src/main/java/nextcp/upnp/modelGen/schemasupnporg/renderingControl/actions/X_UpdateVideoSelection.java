package nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions;

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
public class X_UpdateVideoSelection extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_UpdateVideoSelection.class.getName());
    private ActionInvocation<?> invocation;

    public X_UpdateVideoSelection(Service service, X_UpdateVideoSelectionInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_UpdateVideoSelection"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
//        throw new RuntimeException("(UnsignedIntegerTwoBytesDatatype)");
        getActionInvocation().setInput("VideoEncoding", input.VideoEncoding);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
