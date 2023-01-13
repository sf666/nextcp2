package nextcp.upnp.modelGen.avopenhomeorg.radio1.actions;

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
public class ReadList extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ReadList.class.getName());
    private ActionInvocation<?> invocation;

    public ReadList(Service service, ReadListInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ReadList"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("IdList", input.IdList);
    }

    public ReadListOutput executeAction()
    {
        invocation = execute();

        ReadListOutput result = new ReadListOutput();

  		if (invocation.getOutput("ChannelList").getValue() != null)
  		{
	        result.ChannelList = invocation.getOutput("ChannelList").getValue().toString();
  		}
  		else
  		{
	        result.ChannelList = "";
  		}

        return result;
    }
}
