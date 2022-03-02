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
public class GetSharpness extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSharpness.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public GetSharpness(Service service, GetSharpnessInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSharpness"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetSharpnessOutput executeAction()
    {
        invocation = execute();

        GetSharpnessOutput result = new GetSharpnessOutput();

        result.CurrentSharpness = ((UnsignedIntegerTwoBytes) invocation.getOutput("CurrentSharpness").getValue()).getValue();

        return result;
    }
}
