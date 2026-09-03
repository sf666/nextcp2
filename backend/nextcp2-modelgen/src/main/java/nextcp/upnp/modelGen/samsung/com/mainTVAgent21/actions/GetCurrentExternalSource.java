package nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions;

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
public class GetCurrentExternalSource extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetCurrentExternalSource.class.getName());
    private ActionInvocation<?> invocation;

    public GetCurrentExternalSource(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetCurrentExternalSource"), new NextcpClientInfo()), cp);
		
    }

    public GetCurrentExternalSourceOutput executeAction()
    {
        invocation = execute();

        GetCurrentExternalSourceOutput result = new GetCurrentExternalSourceOutput();

        result.CurrentExternalSource = UpnpValue.toTextOrEmpty(invocation.getOutput("CurrentExternalSource").getValue());
        result.CurrentMBRActivityIndex = UpnpValue.toLong(invocation.getOutput("CurrentMBRActivityIndex").getValue());
        result.ID = UpnpValue.toLong(invocation.getOutput("ID").getValue());
        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
