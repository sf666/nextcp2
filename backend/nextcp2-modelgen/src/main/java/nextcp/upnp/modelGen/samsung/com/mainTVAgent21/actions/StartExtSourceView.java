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
public class StartExtSourceView extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StartExtSourceView.class.getName());
    private ActionInvocation<?> invocation;

    public StartExtSourceView(Service service, StartExtSourceViewInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StartExtSourceView"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("DRMType", UpnpValue.forInput(getActionInvocation(), "DRMType", input.DRMType));
        getActionInvocation().setInput("ForcedFlag", UpnpValue.forInput(getActionInvocation(), "ForcedFlag", input.ForcedFlag));
        getActionInvocation().setInput("ID", UpnpValue.forInput(getActionInvocation(), "ID", input.ID));
        getActionInvocation().setInput("Source", UpnpValue.forInput(getActionInvocation(), "Source", input.Source));
    }

    public StartExtSourceViewOutput executeAction()
    {
        invocation = execute();

        StartExtSourceViewOutput result = new StartExtSourceViewOutput();

        result.ExtSourceViewURL = UpnpValue.toTextOrEmpty(invocation.getOutput("ExtSourceViewURL").getValue());
        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
