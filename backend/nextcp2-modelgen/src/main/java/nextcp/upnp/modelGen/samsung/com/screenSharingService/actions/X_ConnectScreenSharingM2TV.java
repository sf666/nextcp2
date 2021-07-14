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
public class X_ConnectScreenSharingM2TV extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(X_ConnectScreenSharingM2TV.class.getName());
    private ActionInvocation<?> invocation;

    public X_ConnectScreenSharingM2TV(Service service, X_ConnectScreenSharingM2TVInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("X_ConnectScreenSharingM2TV"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("mWlanMacAddress", input.mWlanMacAddress);
        getActionInvocation().setInput("mP2pDeviceAddress", input.mP2pDeviceAddress);
        getActionInvocation().setInput("mBluetoothMacAddress", input.mBluetoothMacAddress);
        getActionInvocation().setInput("mWFDSourcePort", input.mWFDSourcePort);
    }

    public X_ConnectScreenSharingM2TVOutput executeAction()
    {
        invocation = execute();

        X_ConnectScreenSharingM2TVOutput result = new X_ConnectScreenSharingM2TVOutput();

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

        return result;
    }
}
