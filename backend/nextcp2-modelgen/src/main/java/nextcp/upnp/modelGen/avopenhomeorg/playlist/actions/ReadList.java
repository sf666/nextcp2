package nextcp.upnp.modelGen.avopenhomeorg.playlist.actions;

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
public class ReadList extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ReadList.class.getName());
    private ActionInvocation<?> invocation;

    public ReadList(Service service, ReadListInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ReadList")), cp);

        getActionInvocation().setInput("IdList", input.IdList);
    }

    public ReadListOutput executeAction()
    {
        invocation = execute();

        ReadListOutput result = new ReadListOutput();

  		if (invocation.getOutput("TrackList").getValue() != null)
  		{
	        result.TrackList = invocation.getOutput("TrackList").getValue().toString();
  		}
  		else
  		{
	        result.TrackList = "";
  		}

        return result;
    }
}
