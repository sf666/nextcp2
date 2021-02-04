package nextcp.upnp.modelGen.avopenhomeorg.product.actions;

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
public class SourceXml extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SourceXml.class.getName());
    private ActionInvocation<?> invocation;

    public SourceXml(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SourceXml"), new NextcpClientInfo()), cp);

    }

    public SourceXmlOutput executeAction()
    {
        invocation = execute();

        SourceXmlOutput result = new SourceXmlOutput();

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
