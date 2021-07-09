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
public class SetContrast extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetContrast.class.getName());
    private ActionInvocation<?> invocation;

    public SetContrast(Service service, SetContrastInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetContrast"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        throw new RuntimeException("(UnsignedIntegerTwoBytesDatatype)");
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
