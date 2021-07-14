package nextcp.upnp.modelGen.samsung.com.screenSharingService.actions;

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
public class X_ConnectScreenSharingTV2M extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_ConnectScreenSharingTV2M.class.getName());
    private ActionInvocation<?> invocation;

    public X_ConnectScreenSharingTV2M(Service service, X_ConnectScreenSharingTV2MInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_ConnectScreenSharingTV2M"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("mWlanMacAddress", input.mWlanMacAddress);
        getActionInvocation().setInput("mP2pDeviceAddress", input.mP2pDeviceAddress);
        getActionInvocation().setInput("mBluetoothMacAddress", input.mBluetoothMacAddress);
    }

    public X_ConnectScreenSharingTV2MOutput executeAction()
    {
        invocation = execute();

        X_ConnectScreenSharingTV2MOutput result = new X_ConnectScreenSharingTV2MOutput();

  		if (invocation.getOutput("tBSSID").getValue() != null)
  		{
	        result.tBSSID = invocation.getOutput("tBSSID").getValue().toString();
  		}
  		else
  		{
	        result.tBSSID = "";
  		}
  		if (invocation.getOutput("tWlanFreq").getValue() != null)
  		{
	        result.tWlanFreq = invocation.getOutput("tWlanFreq").getValue().toString();
  		}
  		else
  		{
	        result.tWlanFreq = "";
  		}
  		if (invocation.getOutput("tListenFreq").getValue() != null)
  		{
	        result.tListenFreq = invocation.getOutput("tListenFreq").getValue().toString();
  		}
  		else
  		{
	        result.tListenFreq = "";
  		}
  		if (invocation.getOutput("tWFDSourcePort").getValue() != null)
  		{
	        result.tWFDSourcePort = invocation.getOutput("tWFDSourcePort").getValue().toString();
  		}
  		else
  		{
	        result.tWFDSourcePort = "";
  		}

        return result;
    }
}
