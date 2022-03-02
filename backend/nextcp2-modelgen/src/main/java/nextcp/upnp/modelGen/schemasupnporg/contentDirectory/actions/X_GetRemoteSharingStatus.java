package nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions;

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
public class X_GetRemoteSharingStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_GetRemoteSharingStatus.class.getName());
    private ActionInvocation<?> invocation;

    public X_GetRemoteSharingStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_GetRemoteSharingStatus"), new NextcpClientInfo()), cp);

    }

    public X_GetRemoteSharingStatusOutput executeAction()
    {
        invocation = execute();

        X_GetRemoteSharingStatusOutput result = new X_GetRemoteSharingStatusOutput();

        BooleanDatatype data_Status = new BooleanDatatype();
        result.Status = data_Status.valueOf(invocation.getOutput("Status").getValue().toString());

        return result;
    }
}
