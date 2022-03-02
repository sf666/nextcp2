package nextcp.upnp.modelGen.avopenhomeorg.oAuth;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetJobUpdateId;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetJobUpdateIdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetJobUpdateIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearLonglivedLivedToken;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearLonglivedLivedTokenInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetUpdateId;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetUpdateIdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetServiceStatus;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetServiceStatusOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearAllTokens;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearAllTokensInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearToken;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearTokenInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.SetToken;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.SetTokenInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearShortLivedToken;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearShortLivedTokenInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearLonglivedLivedTokens;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearLonglivedLivedTokensInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.BeginLimitedInputFlow;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.BeginLimitedInputFlowOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.BeginLimitedInputFlowInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetPublicKey;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetPublicKeyOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetSupportedServices;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetSupportedServicesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearShortLivedTokens;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.ClearShortLivedTokensInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetJobStatus;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth.actions.GetJobStatusOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class OAuthService
{
    private static Logger log = LoggerFactory.getLogger(OAuthService.class.getName());

    private RemoteService oAuthService = null;

    private UpnpService upnpService = null;

    private OAuthServiceStateVariable oAuthServiceStateVariable = new OAuthServiceStateVariable();
    
    private OAuthServiceSubscription subscription = null;
    
    public OAuthService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        oAuthService = device.findService(new ServiceType("av-openhome-org", "OAuth"));
        if (oAuthService != null)
        {
	        subscription = new OAuthServiceSubscription(oAuthService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'OAuth' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'OAuth' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(IOAuthServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IOAuthServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getOAuthService()
    {
        return oAuthService;
    }    


    public GetJobUpdateIdOutput getJobUpdateId(GetJobUpdateIdInput inp)
    {
        GetJobUpdateId getJobUpdateId = new GetJobUpdateId(oAuthService, inp, upnpService.getControlPoint());
        GetJobUpdateIdOutput res = getJobUpdateId.executeAction();
        return res;        
    }

    public void clearLonglivedLivedToken(ClearLonglivedLivedTokenInput inp)
    {
        ClearLonglivedLivedToken clearLonglivedLivedToken = new ClearLonglivedLivedToken(oAuthService, inp, upnpService.getControlPoint());
        clearLonglivedLivedToken.executeAction();
    }

    public GetUpdateIdOutput getUpdateId()
    {
        GetUpdateId getUpdateId = new GetUpdateId(oAuthService,  upnpService.getControlPoint());
        GetUpdateIdOutput res = getUpdateId.executeAction();
        return res;        
    }

    public GetServiceStatusOutput getServiceStatus()
    {
        GetServiceStatus getServiceStatus = new GetServiceStatus(oAuthService,  upnpService.getControlPoint());
        GetServiceStatusOutput res = getServiceStatus.executeAction();
        return res;        
    }

    public void clearAllTokens(ClearAllTokensInput inp)
    {
        ClearAllTokens clearAllTokens = new ClearAllTokens(oAuthService, inp, upnpService.getControlPoint());
        clearAllTokens.executeAction();
    }

    public void clearToken(ClearTokenInput inp)
    {
        ClearToken clearToken = new ClearToken(oAuthService, inp, upnpService.getControlPoint());
        clearToken.executeAction();
    }

    public void setToken(SetTokenInput inp)
    {
        SetToken setToken = new SetToken(oAuthService, inp, upnpService.getControlPoint());
        setToken.executeAction();
    }

    public void clearShortLivedToken(ClearShortLivedTokenInput inp)
    {
        ClearShortLivedToken clearShortLivedToken = new ClearShortLivedToken(oAuthService, inp, upnpService.getControlPoint());
        clearShortLivedToken.executeAction();
    }

    public void clearLonglivedLivedTokens(ClearLonglivedLivedTokensInput inp)
    {
        ClearLonglivedLivedTokens clearLonglivedLivedTokens = new ClearLonglivedLivedTokens(oAuthService, inp, upnpService.getControlPoint());
        clearLonglivedLivedTokens.executeAction();
    }

    public BeginLimitedInputFlowOutput beginLimitedInputFlow(BeginLimitedInputFlowInput inp)
    {
        BeginLimitedInputFlow beginLimitedInputFlow = new BeginLimitedInputFlow(oAuthService, inp, upnpService.getControlPoint());
        BeginLimitedInputFlowOutput res = beginLimitedInputFlow.executeAction();
        return res;        
    }

    public GetPublicKeyOutput getPublicKey()
    {
        GetPublicKey getPublicKey = new GetPublicKey(oAuthService,  upnpService.getControlPoint());
        GetPublicKeyOutput res = getPublicKey.executeAction();
        return res;        
    }

    public GetSupportedServicesOutput getSupportedServices()
    {
        GetSupportedServices getSupportedServices = new GetSupportedServices(oAuthService,  upnpService.getControlPoint());
        GetSupportedServicesOutput res = getSupportedServices.executeAction();
        return res;        
    }

    public void clearShortLivedTokens(ClearShortLivedTokensInput inp)
    {
        ClearShortLivedTokens clearShortLivedTokens = new ClearShortLivedTokens(oAuthService, inp, upnpService.getControlPoint());
        clearShortLivedTokens.executeAction();
    }

    public GetJobStatusOutput getJobStatus()
    {
        GetJobStatus getJobStatus = new GetJobStatus(oAuthService,  upnpService.getControlPoint());
        GetJobStatusOutput res = getJobStatus.executeAction();
        return res;        
    }
}
