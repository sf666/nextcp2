package nextcp.upnp.modelGen.avopenhomeorg.volume3.actions;

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
public class Characteristics extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Characteristics.class.getName());
    private ActionInvocation<?> invocation;

    public Characteristics(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Characteristics"), new NextcpClientInfo()), cp);
		
    }

    public CharacteristicsOutput executeAction()
    {
        invocation = execute();

        CharacteristicsOutput result = new CharacteristicsOutput();

        result.BalanceMax = UpnpValue.toLong(invocation.getOutput("BalanceMax").getValue());
        result.FadeMax = UpnpValue.toLong(invocation.getOutput("FadeMax").getValue());
        result.VolumeMax = UpnpValue.toLong(invocation.getOutput("VolumeMax").getValue());
        result.VolumeMilliDbPerStep = UpnpValue.toLong(invocation.getOutput("VolumeMilliDbPerStep").getValue());
        result.VolumeSteps = UpnpValue.toLong(invocation.getOutput("VolumeSteps").getValue());
        result.VolumeUnity = UpnpValue.toLong(invocation.getOutput("VolumeUnity").getValue());

        return result;
    }
}
