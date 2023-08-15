package nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions;

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
public class X_GetFeatureList extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_GetFeatureList.class.getName());
    private ActionInvocation<?> invocation;

    public X_GetFeatureList(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_GetFeatureList"), new NextcpClientInfo()), cp);

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
