package nextcp.upnp.modelGen.bubblesoftappscom.main1;

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

import nextcp.upnp.modelGen.bubblesoftappscom.main1.actions.GetVersionInfo;
import nextcp.upnp.modelGen.bubblesoftappscom.main1.actions.GetVersionInfoOutput;
import nextcp.upnp.modelGen.bubblesoftappscom.main1.actions.GetBaseLanURL;
import nextcp.upnp.modelGen.bubblesoftappscom.main1.actions.GetBaseLanURLOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class MainService
{
    private static Logger log = LoggerFactory.getLogger(MainService.class.getName());

    private RemoteService mainService = null;

    private UpnpService upnpService = null;

    private MainServiceStateVariable mainServiceStateVariable = new MainServiceStateVariable();
    
    private MainServiceSubscription subscription = null;
    
    public MainService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        mainService = device.findService(new ServiceType("bubblesoftapps-com", "Main"));
        if (mainService != null)
        {
	        subscription = new MainServiceSubscription(mainService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Main' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Main' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IMainServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IMainServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getMainService()
    {
        return mainService;
    }    


//
// Actions
// =========================================================================
//



    public GetVersionInfoOutput getVersionInfo()
    {
        GetVersionInfo getVersionInfo = new GetVersionInfo(mainService,  upnpService.getControlPoint());
        GetVersionInfoOutput res = getVersionInfo.executeAction();
        return res;        
    }

    public GetBaseLanURLOutput getBaseLanURL()
    {
        GetBaseLanURL getBaseLanURL = new GetBaseLanURL(mainService,  upnpService.getControlPoint());
        GetBaseLanURLOutput res = getBaseLanURL.executeAction();
        return res;        
    }
}
