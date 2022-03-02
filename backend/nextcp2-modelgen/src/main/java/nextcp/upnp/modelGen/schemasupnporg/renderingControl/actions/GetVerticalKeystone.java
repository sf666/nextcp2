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
public class GetVerticalKeystone extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetVerticalKeystone.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public GetVerticalKeystone(Service service, GetVerticalKeystoneInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetVerticalKeystone"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetVerticalKeystoneOutput executeAction()
    {
        invocation = execute();

        GetVerticalKeystoneOutput result = new GetVerticalKeystoneOutput();

        result.CurrentVerticalKeystone = Integer.valueOf(invocation.getOutput("CurrentVerticalKeystone").getValue().toString());

        return result;
    }
}
