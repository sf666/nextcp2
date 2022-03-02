package nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions;

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
 *
 * Template: action.ftl
 *  
 */
public class GetFeatureList extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetFeatureList.class.getName());
    private ActionInvocation<?> invocation;

    public GetFeatureList(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetFeatureList"), new NextcpClientInfo()), cp);

    }

    public GetFeatureListOutput executeAction()
    {
        invocation = execute();

        GetFeatureListOutput result = new GetFeatureListOutput();

  		if (invocation.getOutput("FeatureList").getValue() != null)
  		{
	        result.FeatureList = invocation.getOutput("FeatureList").getValue().toString();
  		}
  		else
  		{
	        result.FeatureList = "";
  		}

        return result;
    }
}
