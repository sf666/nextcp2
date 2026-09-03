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
public class GetMBRDongleStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMBRDongleStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetMBRDongleStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMBRDongleStatus"), new NextcpClientInfo()), cp);
		
    }

    public GetMBRDongleStatusOutput executeAction()
    {
        invocation = execute();

        GetMBRDongleStatusOutput result = new GetMBRDongleStatusOutput();

        result.MBRDongleStatus = UpnpValue.toTextOrEmpty(invocation.getOutput("MBRDongleStatus").getValue());
        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
