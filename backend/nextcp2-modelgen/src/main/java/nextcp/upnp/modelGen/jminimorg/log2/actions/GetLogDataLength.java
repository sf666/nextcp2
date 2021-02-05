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
public class GetLogDataLength extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetLogDataLength.class.getName());
    private ActionInvocation<?> invocation;

    public GetLogDataLength(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetLogDataLength"), new NextcpClientInfo()), cp);

    }

    public GetLogDataLengthOutput executeAction()
    {
        invocation = execute();

        GetLogDataLengthOutput result = new GetLogDataLengthOutput();

        result.DataLength = ((UnsignedIntegerFourBytes) invocation.getOutput("DataLength").getValue()).getValue();

        return result;
    }
}
