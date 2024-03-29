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

        result.Duration = ((UnsignedIntegerFourBytes) invocation.getOutput("Duration").getValue()).getValue();
        result.BitRate = ((UnsignedIntegerFourBytes) invocation.getOutput("BitRate").getValue()).getValue();
        result.BitDepth = ((UnsignedIntegerFourBytes) invocation.getOutput("BitDepth").getValue()).getValue();
        result.SampleRate = ((UnsignedIntegerFourBytes) invocation.getOutput("SampleRate").getValue()).getValue();
        BooleanDatatype data_Lossless = new BooleanDatatype();
        result.Lossless = data_Lossless.valueOf(invocation.getOutput("Lossless").getValue().toString());
  		if (invocation.getOutput("CodecName").getValue() != null)
  		{
	        result.CodecName = invocation.getOutput("CodecName").getValue().toString();
  		}
  		else
  		{
	        result.CodecName = "";
  		}

        return result;
    }
}
