package nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions;

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
public class GetSystemUpdateID extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSystemUpdateID.class.getName());
    private ActionInvocation<?> invocation;

    public GetSystemUpdateID(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSystemUpdateID"), new NextcpClientInfo()), cp);

    }

    public GetSystemUpdateIDOutput executeAction()
    {
        invocation = execute();

        GetSystemUpdateIDOutput result = new GetSystemUpdateIDOutput();

        result.Id = ((UnsignedIntegerFourBytes) invocation.getOutput("Id").getValue()).getValue();

        return result;
    }
}
