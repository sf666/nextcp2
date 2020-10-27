package nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions;

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
public class X_GetFeatureList extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_GetFeatureList.class.getName());
    private ActionInvocation<?> invocation;

    public X_GetFeatureList(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_GetFeatureList")), cp);

    }

    public X_GetFeatureListOutput executeAction()
    {
        invocation = execute();

        X_GetFeatureListOutput result = new X_GetFeatureListOutput();

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
