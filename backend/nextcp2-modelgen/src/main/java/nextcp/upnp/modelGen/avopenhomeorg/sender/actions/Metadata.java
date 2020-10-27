package nextcp.upnp.modelGen.avopenhomeorg.sender.actions;

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
public class Metadata extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Metadata.class.getName());
    private ActionInvocation<?> invocation;

    public Metadata(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Metadata")), cp);

    }

    public MetadataOutput executeAction()
    {
        invocation = execute();

        MetadataOutput result = new MetadataOutput();

  		if (invocation.getOutput("Value").getValue() != null)
  		{
	        result.Value = invocation.getOutput("Value").getValue().toString();
  		}
  		else
  		{
	        result.Value = "";
  		}

        return result;
    }
}
