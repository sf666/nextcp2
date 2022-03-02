package nextcp.upnp.modelGen.magictvcom.magicAudio.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class SetResamplingModeDetails extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetResamplingModeDetails.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public SetResamplingModeDetails(Service service, SetResamplingModeDetailsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetResamplingModeDetails"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Mode", input.Mode);
        getActionInvocation().setInput("FileType", input.FileType);
        getActionInvocation().setInput("SrcSamplingRate", input.SrcSamplingRate);
        getActionInvocation().setInput("NewSamplingRate", input.NewSamplingRate);
        getActionInvocation().setInput("NewBitDepth", input.NewBitDepth);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
