package nextcp.upnp.modelGen.avopenhomeorg.credentials.actions;

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
public class Get extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Get.class.getName());
    private ActionInvocation<?> invocation;

    public Get(Service service, GetInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Get"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Id", input.Id);
    }

    public GetOutput executeAction()
    {
        invocation = execute();

        GetOutput result = new GetOutput();

  		if (invocation.getOutput("UserName").getValue() != null)
  		{
	        result.UserName = invocation.getOutput("UserName").getValue().toString();
  		}
  		else
  		{
	        result.UserName = "";
  		}
        result.Password = (byte[]) invocation.getOutput("Password").getValue();
        BooleanDatatype data_Enabled = new BooleanDatatype();
        result.Enabled = data_Enabled.valueOf(invocation.getOutput("Enabled").getValue().toString());
  		if (invocation.getOutput("Status").getValue() != null)
  		{
	        result.Status = invocation.getOutput("Status").getValue().toString();
  		}
  		else
  		{
	        result.Status = "";
  		}
  		if (invocation.getOutput("Data").getValue() != null)
  		{
	        result.Data = invocation.getOutput("Data").getValue().toString();
  		}
  		else
  		{
	        result.Data = "";
  		}

        return result;
    }
}
