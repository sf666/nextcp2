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
public class GetResamplingModeDetails extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetResamplingModeDetails.class.getName());
    private ActionInvocation<?> invocation;

    public GetResamplingModeDetails(Service service, GetResamplingModeDetailsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetResamplingModeDetails"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("FileType", UpnpValue.forInput(getActionInvocation(), "FileType", input.FileType));
        getActionInvocation().setInput("Mode", UpnpValue.forInput(getActionInvocation(), "Mode", input.Mode));
        getActionInvocation().setInput("ResamplingTag", UpnpValue.forInput(getActionInvocation(), "ResamplingTag", input.ResamplingTag));
        getActionInvocation().setInput("SrcSamplingRate", UpnpValue.forInput(getActionInvocation(), "SrcSamplingRate", input.SrcSamplingRate));
    }

    public GetResamplingModeDetailsOutput executeAction()
    {
        invocation = execute();

        GetResamplingModeDetailsOutput result = new GetResamplingModeDetailsOutput();

        result.NewBitDepth = UpnpValue.toTextOrEmpty(invocation.getOutput("NewBitDepth").getValue());
        result.NewSamplingRate = UpnpValue.toTextOrEmpty(invocation.getOutput("NewSamplingRate").getValue());
        result.ResamplingTag = UpnpValue.toTextOrEmpty(invocation.getOutput("ResamplingTag").getValue());

        return result;
    }
}
