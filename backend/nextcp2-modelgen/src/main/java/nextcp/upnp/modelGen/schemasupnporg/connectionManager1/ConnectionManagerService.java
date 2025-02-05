package nextcp.upnp.modelGen.schemasupnporg.connectionManager1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingRenewal;
import org.jupnp.protocol.sync.SendingSubscribe;
import org.jupnp.protocol.sync.SendingUnsubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions.GetCurrentConnectionIDs;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions.GetCurrentConnectionIDsOutput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions.GetProtocolInfo;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions.GetProtocolInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions.GetCurrentConnectionInfo;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions.GetCurrentConnectionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions.GetCurrentConnectionInfoInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
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

    public void unsubscribeService(UpnpService upnpService, RemoteDevice device)
    {
        SendingUnsubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingUnsubscribe(subscription);
        protocol.run();
    }

    public void renewService(UpnpService upnpService, RemoteDevice device)
    {
        SendingRenewal protocol = upnpService.getControlPoint().getProtocolFactory().createSendingRenewal(subscription);
        protocol.run();
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


//
// Actions
// =========================================================================
//



    public GetCurrentConnectionIDsOutput getCurrentConnectionIDs()
    {
        GetCurrentConnectionIDs getCurrentConnectionIDs = new GetCurrentConnectionIDs(connectionManagerService,  upnpService.getControlPoint());
        GetCurrentConnectionIDsOutput res = getCurrentConnectionIDs.executeAction();
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
