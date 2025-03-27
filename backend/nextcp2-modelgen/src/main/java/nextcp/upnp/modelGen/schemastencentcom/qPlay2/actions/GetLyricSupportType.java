package nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions;

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
public class GetLyricSupportType extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetLyricSupportType.class.getName());
    private ActionInvocation<?> invocation;

    public GetLyricSupportType(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetLyricSupportType"), new NextcpClientInfo()), cp);

    }

    public GetLyricSupportTypeOutput executeAction()
    {
        invocation = execute();

        GetLyricSupportTypeOutput result = new GetLyricSupportTypeOutput();

  		if (invocation.getOutput("LyricType").getValue() != null)
  		{
	        result.LyricType = invocation.getOutput("LyricType").getValue().toString();
  		}
  		else
  		{
	        result.LyricType = "";
  		}

        return result;
    }
}
