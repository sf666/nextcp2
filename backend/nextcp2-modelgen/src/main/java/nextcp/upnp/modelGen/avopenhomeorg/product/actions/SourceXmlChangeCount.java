package nextcp.upnp.modelGen.avopenhomeorg.product.actions;

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
public class SourceXmlChangeCount extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SourceXmlChangeCount.class.getName());
    private ActionInvocation<?> invocation;

    public SourceXmlChangeCount(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SourceXmlChangeCount")), cp);

    }

    public SourceXmlChangeCountOutput executeAction()
    {
        invocation = execute();

        SourceXmlChangeCountOutput result = new SourceXmlChangeCountOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
