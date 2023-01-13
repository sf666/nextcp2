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
public class GetResamplingModeDetails extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetResamplingModeDetails.class.getName());
    private ActionInvocation<?> invocation;

    public GetResamplingModeDetails(Service service, GetResamplingModeDetailsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetResamplingModeDetails"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ResamplingTag", input.ResamplingTag);
        getActionInvocation().setInput("Mode", input.Mode);
        getActionInvocation().setInput("FileType", input.FileType);
        getActionInvocation().setInput("SrcSamplingRate", input.SrcSamplingRate);
    }

    public GetResamplingModeDetailsOutput executeAction()
    {
        invocation = execute();

        GetResamplingModeDetailsOutput result = new GetResamplingModeDetailsOutput();

  		if (invocation.getOutput("NewSamplingRate").getValue() != null)
  		{
	        result.NewSamplingRate = invocation.getOutput("NewSamplingRate").getValue().toString();
  		}
  		else
  		{
	        result.NewSamplingRate = "";
  		}
  		if (invocation.getOutput("NewBitDepth").getValue() != null)
  		{
	        result.NewBitDepth = invocation.getOutput("NewBitDepth").getValue().toString();
  		}
  		else
  		{
	        result.NewBitDepth = "";
  		}
  		if (invocation.getOutput("ResamplingTag").getValue() != null)
  		{
	        result.ResamplingTag = invocation.getOutput("ResamplingTag").getValue().toString();
  		}
  		else
  		{
	        result.ResamplingTag = "";
  		}

        return result;
    }
}
