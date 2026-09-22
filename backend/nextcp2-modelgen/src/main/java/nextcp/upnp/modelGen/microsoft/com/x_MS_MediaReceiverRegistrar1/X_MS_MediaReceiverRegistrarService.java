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

import nextcp.upnp.GenActionException;
import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetAuthorizationDeniedUpdateID;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetAuthorizationDeniedUpdateIDOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetAuthorizationGrantedUpdateID;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetAuthorizationGrantedUpdateIDOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetValidationRevokedUpdateID;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetValidationRevokedUpdateIDOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetValidationSucceededUpdateID;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.GetValidationSucceededUpdateIDOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsAuthorized;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsAuthorizedOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsAuthorizedInput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsValidated;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsValidatedOutput;
import nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions.IsValidatedInput;
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
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public X_MS_MediaReceiverRegistrarService(UpnpService upnpService, RemoteDevice device, IX_MS_MediaReceiverRegistrarServiceEventListener listener)
    {
        this.upnpService = upnpService;
        x_MS_MediaReceiverRegistrarService = device.findService(new ServiceType("microsoft.com", "X_MS_MediaReceiverRegistrar"));
        if (x_MS_MediaReceiverRegistrarService != null)
        {
	        subscription = new X_MS_MediaReceiverRegistrarServiceSubscription(x_MS_MediaReceiverRegistrarService, 600);
	        if (listener != null)
	        {
	            subscription.addSubscriptionEventListener(listener);
	        }
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

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return x_MS_MediaReceiverRegistrarService != null && x_MS_MediaReceiverRegistrarService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public GetAuthorizationDeniedUpdateIDOutput getAuthorizationDeniedUpdateID()
    {
        if (!hasAction("GetAuthorizationDeniedUpdateID"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAuthorizationDeniedUpdateID of service X_MS_MediaReceiverRegistrar");
        }
        GetAuthorizationDeniedUpdateID getAuthorizationDeniedUpdateID = new GetAuthorizationDeniedUpdateID(x_MS_MediaReceiverRegistrarService,  upnpService.getControlPoint());
        GetAuthorizationDeniedUpdateIDOutput res = getAuthorizationDeniedUpdateID.executeAction();
        return res;        
    }

    public GetAuthorizationGrantedUpdateIDOutput getAuthorizationGrantedUpdateID()
    {
        if (!hasAction("GetAuthorizationGrantedUpdateID"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAuthorizationGrantedUpdateID of service X_MS_MediaReceiverRegistrar");
        }
        GetAuthorizationGrantedUpdateID getAuthorizationGrantedUpdateID = new GetAuthorizationGrantedUpdateID(x_MS_MediaReceiverRegistrarService,  upnpService.getControlPoint());
        GetAuthorizationGrantedUpdateIDOutput res = getAuthorizationGrantedUpdateID.executeAction();
        return res;        
    }

    public GetValidationRevokedUpdateIDOutput getValidationRevokedUpdateID()
    {
        if (!hasAction("GetValidationRevokedUpdateID"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetValidationRevokedUpdateID of service X_MS_MediaReceiverRegistrar");
        }
        GetValidationRevokedUpdateID getValidationRevokedUpdateID = new GetValidationRevokedUpdateID(x_MS_MediaReceiverRegistrarService,  upnpService.getControlPoint());
        GetValidationRevokedUpdateIDOutput res = getValidationRevokedUpdateID.executeAction();
        return res;        
    }

    public GetValidationSucceededUpdateIDOutput getValidationSucceededUpdateID()
    {
        if (!hasAction("GetValidationSucceededUpdateID"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetValidationSucceededUpdateID of service X_MS_MediaReceiverRegistrar");
        }
        GetValidationSucceededUpdateID getValidationSucceededUpdateID = new GetValidationSucceededUpdateID(x_MS_MediaReceiverRegistrarService,  upnpService.getControlPoint());
        GetValidationSucceededUpdateIDOutput res = getValidationSucceededUpdateID.executeAction();
        return res;        
    }

    public IsAuthorizedOutput isAuthorized(IsAuthorizedInput inp)
    {
        if (!hasAction("IsAuthorized"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action IsAuthorized of service X_MS_MediaReceiverRegistrar");
        }
        IsAuthorized isAuthorized = new IsAuthorized(x_MS_MediaReceiverRegistrarService, inp, upnpService.getControlPoint());
        IsAuthorizedOutput res = isAuthorized.executeAction();
        return res;        
    }

    public IsValidatedOutput isValidated(IsValidatedInput inp)
    {
        if (!hasAction("IsValidated"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action IsValidated of service X_MS_MediaReceiverRegistrar");
        }
        IsValidated isValidated = new IsValidated(x_MS_MediaReceiverRegistrarService, inp, upnpService.getControlPoint());
        IsValidatedOutput res = isValidated.executeAction();
        return res;        
    }

    public RegisterDeviceOutput registerDevice(RegisterDeviceInput inp)
    {
        if (!hasAction("RegisterDevice"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action RegisterDevice of service X_MS_MediaReceiverRegistrar");
        }
        RegisterDevice registerDevice = new RegisterDevice(x_MS_MediaReceiverRegistrarService, inp, upnpService.getControlPoint());
        RegisterDeviceOutput res = registerDevice.executeAction();
        return res;        
    }
}
