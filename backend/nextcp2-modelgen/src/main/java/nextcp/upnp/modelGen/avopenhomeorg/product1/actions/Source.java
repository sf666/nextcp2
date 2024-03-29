package nextcp.upnp.modelGen.avopenhomeorg.product1.actions;

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
public class Source extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Source.class.getName());
    private ActionInvocation<?> invocation;

    public Source(Service service, SourceInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Source"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Index", new UnsignedIntegerFourBytes(input.Index));
    }

    public SourceOutput executeAction()
    {
        invocation = execute();

        SourceOutput result = new SourceOutput();

  		if (invocation.getOutput("SystemName").getValue() != null)
  		{
	        result.SystemName = invocation.getOutput("SystemName").getValue().toString();
  		}
  		else
  		{
	        result.SystemName = "";
  		}
  		if (invocation.getOutput("Type").getValue() != null)
  		{
	        result.Type = invocation.getOutput("Type").getValue().toString();
  		}
  		else
  		{
	        result.Type = "";
  		}
  		if (invocation.getOutput("Name").getValue() != null)
  		{
	        result.Name = invocation.getOutput("Name").getValue().toString();
  		}
  		else
  		{
	        result.Name = "";
  		}
        BooleanDatatype data_Visible = new BooleanDatatype();
        result.Visible = data_Visible.valueOf(invocation.getOutput("Visible").getValue().toString());

        return result;
    }
}
