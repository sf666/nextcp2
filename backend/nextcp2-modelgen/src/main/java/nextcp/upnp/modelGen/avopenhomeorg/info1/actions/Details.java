package nextcp.upnp.modelGen.avopenhomeorg.info1.actions;

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
public class Details extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Details.class.getName());
    private ActionInvocation<?> invocation;

    public Details(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Details"), new NextcpClientInfo()), cp);
		
    }

    public DetailsOutput executeAction()
    {
        invocation = execute();

        DetailsOutput result = new DetailsOutput();

        result.BitDepth = UpnpValue.toLong(invocation.getOutput("BitDepth").getValue());
        result.BitRate = UpnpValue.toLong(invocation.getOutput("BitRate").getValue());
        result.CodecName = UpnpValue.toTextOrEmpty(invocation.getOutput("CodecName").getValue());
        result.Duration = UpnpValue.toLong(invocation.getOutput("Duration").getValue());
        result.Lossless = UpnpValue.toBoolean(invocation.getOutput("Lossless").getValue());
        result.SampleRate = UpnpValue.toLong(invocation.getOutput("SampleRate").getValue());

        return result;
    }
}
