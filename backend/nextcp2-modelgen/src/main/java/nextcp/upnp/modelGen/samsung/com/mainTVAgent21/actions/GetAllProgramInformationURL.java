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
public class GetAllProgramInformationURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAllProgramInformationURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetAllProgramInformationURL(Service service, GetAllProgramInformationURLInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAllProgramInformationURL"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("AntennaMode", UpnpValue.forInput(getActionInvocation(), "AntennaMode", input.AntennaMode));
        getActionInvocation().setInput("Channel", UpnpValue.forInput(getActionInvocation(), "Channel", input.Channel));
    }

    public GetAllProgramInformationURLOutput executeAction()
    {
        invocation = execute();

        GetAllProgramInformationURLOutput result = new GetAllProgramInformationURLOutput();

        result.AllProgramInformationURL = UpnpValue.toTextOrEmpty(invocation.getOutput("AllProgramInformationURL").getValue());
        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
