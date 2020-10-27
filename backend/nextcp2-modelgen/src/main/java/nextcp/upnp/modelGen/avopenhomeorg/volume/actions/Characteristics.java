package nextcp.upnp.modelGen.avopenhomeorg.volume.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class Characteristics extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Characteristics.class.getName());
    private ActionInvocation<?> invocation;

    public Characteristics(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Characteristics")), cp);

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
