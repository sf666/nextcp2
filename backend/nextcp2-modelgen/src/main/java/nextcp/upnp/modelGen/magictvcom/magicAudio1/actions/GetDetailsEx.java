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
import nextcp.upnp.UpnpValue;

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

        result.FormatConversion = UpnpValue.toTextOrEmpty(invocation.getOutput("FormatConversion").getValue());
        result.MQAAuthenticity = UpnpValue.toTextOrEmpty(invocation.getOutput("MQAAuthenticity").getValue());
        result.MQACreatorId = UpnpValue.toLong(invocation.getOutput("MQACreatorId").getValue());
        result.MQAProvenance = UpnpValue.toTextOrEmpty(invocation.getOutput("MQAProvenance").getValue());
        result.MQASampleRate = UpnpValue.toLong(invocation.getOutput("MQASampleRate").getValue());
        result.OutputBitDepth = UpnpValue.toLong(invocation.getOutput("OutputBitDepth").getValue());
        result.OutputDeemphasis = UpnpValue.toBoolean(invocation.getOutput("OutputDeemphasis").getValue());
        result.OutputInvertPhase = UpnpValue.toBoolean(invocation.getOutput("OutputInvertPhase").getValue());
        result.OutputSampleRate = UpnpValue.toLong(invocation.getOutput("OutputSampleRate").getValue());
        result.ReplayGain = UpnpValue.toTextOrEmpty(invocation.getOutput("ReplayGain").getValue());

        return result;
    }
}
