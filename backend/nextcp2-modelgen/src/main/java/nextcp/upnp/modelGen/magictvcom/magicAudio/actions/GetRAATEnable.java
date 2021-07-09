package nextcp.upnp.modelGen.magictvcom.magicAudio.actions;

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
public class GetRAATEnable extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRAATEnable.class.getName());
    private ActionInvocation<?> invocation;

    public GetRAATEnable(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRAATEnable"), new NextcpClientInfo()), cp);

    }

    public GetRAATEnableOutput executeAction()
    {
        invocation = execute();

        GetRAATEnableOutput result = new GetRAATEnableOutput();

        BooleanDatatype data_Value = new BooleanDatatype();
        result.Value = data_Value.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
