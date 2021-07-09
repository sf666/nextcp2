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
public class GetTidalConnectEnable extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetTidalConnectEnable.class.getName());
    private ActionInvocation<?> invocation;

    public GetTidalConnectEnable(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetTidalConnectEnable"), new NextcpClientInfo()), cp);

    }

    public GetTidalConnectEnableOutput executeAction()
    {
        invocation = execute();

        GetTidalConnectEnableOutput result = new GetTidalConnectEnableOutput();

        BooleanDatatype data_Value = new BooleanDatatype();
        result.Value = data_Value.valueOf(invocation.getOutput("Value").getValue().toString());

        return result;
    }
}
