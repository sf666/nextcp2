package nextcp.upnp.modelGen.avopenhomeorg.product2.actions;

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
public class Source extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Source.class.getName());
    private ActionInvocation<?> invocation;

    public Source(Service service, SourceInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Source"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Index", UpnpValue.forInput(getActionInvocation(), "Index", input.Index));
    }

    public SourceOutput executeAction()
    {
        invocation = execute();

        SourceOutput result = new SourceOutput();

        result.Name = UpnpValue.toTextOrEmpty(invocation.getOutput("Name").getValue());
        result.SystemName = UpnpValue.toTextOrEmpty(invocation.getOutput("SystemName").getValue());
        result.Type = UpnpValue.toTextOrEmpty(invocation.getOutput("Type").getValue());
        result.Visible = UpnpValue.toBoolean(invocation.getOutput("Visible").getValue());

        return result;
    }
}
