package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class GetDetailsEx extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetDetailsEx.class.getName());
    private ActionInvocation<?> invocation;

    public GetDetailsEx(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetDetailsEx"), new NextcpClientInfo()), cp);

    }

    public GetDetailsExOutput executeAction()
    {
        invocation = execute();

        GetDetailsExOutput result = new GetDetailsExOutput();

  		if (invocation.getOutput("FormatConversion").getValue() != null)
  		{
	        result.FormatConversion = invocation.getOutput("FormatConversion").getValue().toString();
  		}
  		else
  		{
	        result.FormatConversion = "";
  		}
        result.OutputBitDepth = ((UnsignedIntegerFourBytes) invocation.getOutput("OutputBitDepth").getValue()).getValue();
        result.OutputSampleRate = ((UnsignedIntegerFourBytes) invocation.getOutput("OutputSampleRate").getValue()).getValue();
        BooleanDatatype data_OutputDeemphasis = new BooleanDatatype();
        result.OutputDeemphasis = data_OutputDeemphasis.valueOf(invocation.getOutput("OutputDeemphasis").getValue().toString());
        BooleanDatatype data_OutputInvertPhase = new BooleanDatatype();
        result.OutputInvertPhase = data_OutputInvertPhase.valueOf(invocation.getOutput("OutputInvertPhase").getValue().toString());
        result.MQASampleRate = ((UnsignedIntegerFourBytes) invocation.getOutput("MQASampleRate").getValue()).getValue();
  		if (invocation.getOutput("MQAAuthenticity").getValue() != null)
  		{
	        result.MQAAuthenticity = invocation.getOutput("MQAAuthenticity").getValue().toString();
  		}
  		else
  		{
	        result.MQAAuthenticity = "";
  		}
  		if (invocation.getOutput("MQAProvenance").getValue() != null)
  		{
	        result.MQAProvenance = invocation.getOutput("MQAProvenance").getValue().toString();
  		}
  		else
  		{
	        result.MQAProvenance = "";
  		}
        result.MQACreatorId = ((UnsignedIntegerFourBytes) invocation.getOutput("MQACreatorId").getValue()).getValue();

        return result;
    }
}
