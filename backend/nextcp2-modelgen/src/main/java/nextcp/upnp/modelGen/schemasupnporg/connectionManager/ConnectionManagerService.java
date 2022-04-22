package nextcp.upnp.modelGen.schemasupnporg.connectionManager;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.GetCurrentConnectionIDs;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.GetCurrentConnectionIDsOutput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.ConnectionComplete;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.ConnectionCompleteInput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.PrepareForConnection;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.PrepareForConnectionOutput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.PrepareForConnectionInput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.GetProtocolInfo;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.GetProtocolInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.GetCurrentConnectionInfo;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.GetCurrentConnectionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions.GetCurrentConnectionInfoInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class ConnectionManagerService
{
    private static Logger log = LoggerFactory.getLogger(ConnectionManagerService.class.getName());

    private RemoteService connectionManagerService = null;

    private UpnpService upnpService = null;

    private ConnectionManagerServiceStateVariable connectionManagerServiceStateVariable = new ConnectionManagerServiceStateVariable();
    
    private ConnectionManagerServiceSubscription subscription = null;
    
    public ConnectionManagerService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        connectionManagerService = device.findService(new ServiceType("schemas-upnp-org", "ConnectionManager"));
        if (connectionManagerService != null)
        {
	        subscription = new ConnectionManagerServiceSubscription(connectionManagerService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'ConnectionManager' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'ConnectionManager' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(IConnectionManagerServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IConnectionManagerServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getConnectionManagerService()
    {
        return connectionManagerService;
    }    


    public GetCurrentConnectionIDsOutput getCurrentConnectionIDs()
    {
        GetCurrentConnectionIDs getCurrentConnectionIDs = new GetCurrentConnectionIDs(connectionManagerService,  upnpService.getControlPoint());
        GetCurrentConnectionIDsOutput res = getCurrentConnectionIDs.executeAction();
        return res;        
    }

    public void connectionComplete(ConnectionCompleteInput inp)
    {
        ConnectionComplete connectionComplete = new ConnectionComplete(connectionManagerService, inp, upnpService.getControlPoint());
        connectionComplete.executeAction();
    }

    public PrepareForConnectionOutput prepareForConnection(PrepareForConnectionInput inp)
    {
        PrepareForConnection prepareForConnection = new PrepareForConnection(connectionManagerService, inp, upnpService.getControlPoint());
        PrepareForConnectionOutput res = prepareForConnection.executeAction();
        return res;        
    }

    public GetProtocolInfoOutput getProtocolInfo()
    {
        GetProtocolInfo getProtocolInfo = new GetProtocolInfo(connectionManagerService,  upnpService.getControlPoint());
        GetProtocolInfoOutput res = getProtocolInfo.executeAction();
        return res;        
    }

    public GetCurrentConnectionInfoOutput getCurrentConnectionInfo(GetCurrentConnectionInfoInput inp)
    {
        GetCurrentConnectionInfo getCurrentConnectionInfo = new GetCurrentConnectionInfo(connectionManagerService, inp, upnpService.getControlPoint());
        GetCurrentConnectionInfoOutput res = getCurrentConnectionInfo.executeAction();
        return res;        
    }
}
