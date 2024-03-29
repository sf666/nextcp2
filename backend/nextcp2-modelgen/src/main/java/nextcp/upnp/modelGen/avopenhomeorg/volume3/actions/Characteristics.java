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

        result.VolumeMax = ((UnsignedIntegerFourBytes) invocation.getOutput("VolumeMax").getValue()).getValue();
        result.VolumeUnity = ((UnsignedIntegerFourBytes) invocation.getOutput("VolumeUnity").getValue()).getValue();
        result.VolumeSteps = ((UnsignedIntegerFourBytes) invocation.getOutput("VolumeSteps").getValue()).getValue();
        result.VolumeMilliDbPerStep = ((UnsignedIntegerFourBytes) invocation.getOutput("VolumeMilliDbPerStep").getValue()).getValue();
        result.BalanceMax = ((UnsignedIntegerFourBytes) invocation.getOutput("BalanceMax").getValue()).getValue();
        result.FadeMax = ((UnsignedIntegerFourBytes) invocation.getOutput("FadeMax").getValue()).getValue();

        return result;
    }
}
