package nextcp.upnp.modelGen.avopenhomeorg.oAuth1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetJobUpdateId;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetJobUpdateIdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetJobUpdateIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetUpdateId;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetUpdateIdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearLonglivedLivedToken;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearLonglivedLivedTokenInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetServiceStatus;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetServiceStatusOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearAllTokens;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearAllTokensInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearToken;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearTokenInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.SetToken;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.SetTokenInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearLonglivedLivedTokens;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearLonglivedLivedTokensInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearShortLivedToken;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearShortLivedTokenInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.BeginLimitedInputFlow;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.BeginLimitedInputFlowOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.BeginLimitedInputFlowInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetPublicKey;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetPublicKeyOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetSupportedServices;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetSupportedServicesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearShortLivedTokens;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.ClearShortLivedTokensInput;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetJobStatus;
import nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions.GetJobStatusOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
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

    public GetUpdateIdOutput getUpdateId()
    {
        GetUpdateId getUpdateId = new GetUpdateId(oAuthService,  upnpService.getControlPoint());
        GetUpdateIdOutput res = getUpdateId.executeAction();
        return res;        
    }

    public void clearLonglivedLivedToken(ClearLonglivedLivedTokenInput inp)
    {
        ClearLonglivedLivedToken clearLonglivedLivedToken = new ClearLonglivedLivedToken(oAuthService, inp, upnpService.getControlPoint());
        clearLonglivedLivedToken.executeAction();
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

    public void clearLonglivedLivedTokens(ClearLonglivedLivedTokensInput inp)
    {
        ClearLonglivedLivedTokens clearLonglivedLivedTokens = new ClearLonglivedLivedTokens(oAuthService, inp, upnpService.getControlPoint());
        clearLonglivedLivedTokens.executeAction();
    }

    public void clearShortLivedToken(ClearShortLivedTokenInput inp)
    {
        ClearShortLivedToken clearShortLivedToken = new ClearShortLivedToken(oAuthService, inp, upnpService.getControlPoint());
        clearShortLivedToken.executeAction();
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
