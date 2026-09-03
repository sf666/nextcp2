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
public class GetAPInformation extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAPInformation.class.getName());
    private ActionInvocation<?> invocation;

    public GetAPInformation(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAPInformation"), new NextcpClientInfo()), cp);
		
    }

    public GetAPInformationOutput executeAction()
    {
        invocation = execute();

        GetAPInformationOutput result = new GetAPInformationOutput();

        result.APInformation = UpnpValue.toTextOrEmpty(invocation.getOutput("APInformation").getValue());
        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
