package nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions;

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
 *
 * Template: action.ftl
 *  
 */
public class GetJobUpdateId extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetJobUpdateId.class.getName());
    private ActionInvocation<?> invocation;

    public GetJobUpdateId(Service service, GetJobUpdateIdInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetJobUpdateId"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ServiceId", input.ServiceId);
    }

    public GetJobUpdateIdOutput executeAction()
    {
        invocation = execute();

        GetJobUpdateIdOutput result = new GetJobUpdateIdOutput();

        result.JobUpdateId = ((UnsignedIntegerFourBytes) invocation.getOutput("JobUpdateId").getValue()).getValue();

        return result;
    }
}
