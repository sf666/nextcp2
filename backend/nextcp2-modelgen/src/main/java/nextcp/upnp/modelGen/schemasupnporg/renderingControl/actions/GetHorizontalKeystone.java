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
public class GetHorizontalKeystone extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetHorizontalKeystone.class.getName());
    private ActionInvocation<?> invocation;

    public GetHorizontalKeystone(Service service, GetHorizontalKeystoneInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetHorizontalKeystone"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetHorizontalKeystoneOutput executeAction()
    {
        invocation = execute();

        GetHorizontalKeystoneOutput result = new GetHorizontalKeystoneOutput();

        result.CurrentHorizontalKeystone = Integer.valueOf(invocation.getOutput("CurrentHorizontalKeystone").getValue().toString());

        return result;
    }
}
