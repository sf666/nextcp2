package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1;

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

import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetAuthorizationGrantedUpdateID;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetAuthorizationGrantedUpdateIDOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsValidated;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsValidatedOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsValidatedInput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetValidationSucceededUpdateID;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetValidationSucceededUpdateIDOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetAuthorizationDeniedUpdateID;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetAuthorizationDeniedUpdateIDOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetValidationRevokedUpdateID;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetValidationRevokedUpdateIDOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsAuthorized;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsAuthorizedOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsAuthorizedInput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.RegisterDevice;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.RegisterDeviceOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.RegisterDeviceInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class X_MS_MediaReceiverRegistrarService
{
    private static Logger log = LoggerFactory.getLogger(X_MS_MediaReceiverRegistrarService.class.getName());

    private RemoteService x_MS_MediaReceiverRegistrarService = null;

    private UpnpService upnpService = null;

//    private X_MS_MediaReceiverRegistrarServiceStateVariable x_MS_MediaReceiverRegistrarServiceStateVariable = new X_MS_MediaReceiverRegistrarServiceStateVariable();
    
    private X_MS_MediaReceiverRegistrarServiceSubscription subscription = null;
    
    public X_MS_MediaReceiverRegistrarService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        x_MS_MediaReceiverRegistrarService = device.findService(new ServiceType("microsoft.com", "X_MS_MediaReceiverRegistrar"));
        if (x_MS_MediaReceiverRegistrarService != null)
        {
	        subscription = new X_MS_MediaReceiverRegistrarServiceSubscription(x_MS_MediaReceiverRegistrarService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'X_MS_MediaReceiverRegistrar' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'X_MS_MediaReceiverRegistrar' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IX_MS_MediaReceiverRegistrarServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IX_MS_MediaReceiverRegistrarServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getX_MS_MediaReceiverRegistrarService()
    {
        return x_MS_MediaReceiverRegistrarService;
    }    


//
// Actions
// =========================================================================
//



    public GetAuthorizationGrantedUpdateIDOutput getAuthorizationGrantedUpdateID()
    {
        GetAuthorizationGrantedUpdateID getAuthorizationGrantedUpdateID = new GetAuthorizationGrantedUpdateID(x_MS_MediaReceiverRegistrarService,  upnpService.getControlPoint());
        GetAuthorizationGrantedUpdateIDOutput res = getAuthorizationGrantedUpdateID.executeAction();
        return res;        
    }

    public IsValidatedOutput isValidated(IsValidatedInput inp)
    {
        IsValidated isValidated = new IsValidated(x_MS_MediaReceiverRegistrarService, inp, upnpService.getControlPoint());
        IsValidatedOutput res = isValidated.executeAction();
        return res;        
    }

    public GetValidationSucceededUpdateIDOutput getValidationSucceededUpdateID()
    {
        GetValidationSucceededUpdateID getValidationSucceededUpdateID = new GetValidationSucceededUpdateID(x_MS_MediaReceiverRegistrarService,  upnpService.getControlPoint());
        GetValidationSucceededUpdateIDOutput res = getValidationSucceededUpdateID.executeAction();
        return res;        
    }

    public GetAuthorizationDeniedUpdateIDOutput getAuthorizationDeniedUpdateID()
    {
        GetAuthorizationDeniedUpdateID getAuthorizationDeniedUpdateID = new GetAuthorizationDeniedUpdateID(x_MS_MediaReceiverRegistrarService,  upnpService.getControlPoint());
        GetAuthorizationDeniedUpdateIDOutput res = getAuthorizationDeniedUpdateID.executeAction();
        return res;        
    }

    public GetValidationRevokedUpdateIDOutput getValidationRevokedUpdateID()
    {
        GetValidationRevokedUpdateID getValidationRevokedUpdateID = new GetValidationRevokedUpdateID(x_MS_MediaReceiverRegistrarService,  upnpService.getControlPoint());
        GetValidationRevokedUpdateIDOutput res = getValidationRevokedUpdateID.executeAction();
        return res;        
    }

    public IsAuthorizedOutput isAuthorized(IsAuthorizedInput inp)
    {
        IsAuthorized isAuthorized = new IsAuthorized(x_MS_MediaReceiverRegistrarService, inp, upnpService.getControlPoint());
        IsAuthorizedOutput res = isAuthorized.executeAction();
        return res;        
    }

    public RegisterDeviceOutput registerDevice(RegisterDeviceInput inp)
    {
        RegisterDevice registerDevice = new RegisterDevice(x_MS_MediaReceiverRegistrarService, inp, upnpService.getControlPoint());
        RegisterDeviceOutput res = registerDevice.executeAction();
        return res;        
    }
}
