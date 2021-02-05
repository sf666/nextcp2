package nextcp.upnp.modelGen.jminimorg.log2.actions;

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
public class GetLogStart extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetLogStart.class.getName());
    private ActionInvocation<?> invocation;

    public GetLogStart(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetLogStart"), new NextcpClientInfo()), cp);

    }

    public GetLogStartOutput executeAction()
    {
        invocation = execute();

        GetLogStartOutput result = new GetLogStartOutput();

        result.StartPosition = ((UnsignedIntegerFourBytes) invocation.getOutput("StartPosition").getValue()).getValue();

        return result;
    }
}
