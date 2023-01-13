package nextcp.upnp.modelGen.avopenhomeorg.credentials1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.SetEnabled;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.SetEnabledInput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.Set;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.SetInput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.GetPublicKey;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.GetPublicKeyOutput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.Get;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.GetOutput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.GetInput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.GetSequenceNumber;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.GetSequenceNumberOutput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.Login;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.LoginOutput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.LoginInput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.ReLogin;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.ReLoginOutput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.ReLoginInput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.Clear;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.ClearInput;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.GetIds;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions.GetIdsOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class CredentialsService
{
    private static Logger log = LoggerFactory.getLogger(CredentialsService.class.getName());

    private RemoteService credentialsService = null;

    private UpnpService upnpService = null;

    private CredentialsServiceStateVariable credentialsServiceStateVariable = new CredentialsServiceStateVariable();
    
    private CredentialsServiceSubscription subscription = null;
    
    public CredentialsService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        credentialsService = device.findService(new ServiceType("av-openhome-org", "Credentials"));
        if (credentialsService != null)
        {
	        subscription = new CredentialsServiceSubscription(credentialsService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Credentials' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Credentials' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(ICredentialsServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(ICredentialsServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getCredentialsService()
    {
        return credentialsService;
    }    


    public void setEnabled(SetEnabledInput inp)
    {
        SetEnabled setEnabled = new SetEnabled(credentialsService, inp, upnpService.getControlPoint());
        setEnabled.executeAction();
    }

    public void set(SetInput inp)
    {
        Set set = new Set(credentialsService, inp, upnpService.getControlPoint());
        set.executeAction();
    }

    public GetPublicKeyOutput getPublicKey()
    {
        GetPublicKey getPublicKey = new GetPublicKey(credentialsService,  upnpService.getControlPoint());
        GetPublicKeyOutput res = getPublicKey.executeAction();
        return res;        
    }

    public GetOutput get(GetInput inp)
    {
        Get get = new Get(credentialsService, inp, upnpService.getControlPoint());
        GetOutput res = get.executeAction();
        return res;        
    }

    public GetSequenceNumberOutput getSequenceNumber()
    {
        GetSequenceNumber getSequenceNumber = new GetSequenceNumber(credentialsService,  upnpService.getControlPoint());
        GetSequenceNumberOutput res = getSequenceNumber.executeAction();
        return res;        
    }

    public LoginOutput login(LoginInput inp)
    {
        Login login = new Login(credentialsService, inp, upnpService.getControlPoint());
        LoginOutput res = login.executeAction();
        return res;        
    }

    public ReLoginOutput reLogin(ReLoginInput inp)
    {
        ReLogin reLogin = new ReLogin(credentialsService, inp, upnpService.getControlPoint());
        ReLoginOutput res = reLogin.executeAction();
        return res;        
    }

    public void clear(ClearInput inp)
    {
        Clear clear = new Clear(credentialsService, inp, upnpService.getControlPoint());
        clear.executeAction();
    }

    public GetIdsOutput getIds()
    {
        GetIds getIds = new GetIds(credentialsService,  upnpService.getControlPoint());
        GetIdsOutput res = getIds.executeAction();
        return res;        
    }
}
