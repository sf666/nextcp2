package nextcp.upnp.modelGen.schemasupnporg.aVTransport1;

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

import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.Pause;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.PauseInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.Stop;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.StopInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetPositionInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetPositionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetPositionInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SetPlayMode;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SetPlayModeInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SetNextAVTransportURI;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SetNextAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.Play;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.PlayInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetDeviceCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetDeviceCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetDeviceCapabilitiesInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetMediaInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetMediaInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetMediaInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.Next;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.NextInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetTransportInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetTransportInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetTransportInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.Previous;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.PreviousInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetTransportSettings;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetTransportSettingsOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetTransportSettingsInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SetAVTransportURI;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SetAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetCurrentTransportActions;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetCurrentTransportActionsOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetCurrentTransportActionsInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.Seek;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SeekInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class AVTransportService
{
    private static Logger log = LoggerFactory.getLogger(AVTransportService.class.getName());

    private RemoteService aVTransportService = null;

    private UpnpService upnpService = null;

    private AVTransportServiceStateVariable aVTransportServiceStateVariable = new AVTransportServiceStateVariable();
    
    private AVTransportServiceSubscription subscription = null;
    
    public AVTransportService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        aVTransportService = device.findService(new ServiceType("schemas-upnp-org", "AVTransport"));
        if (aVTransportService != null)
        {
	        subscription = new AVTransportServiceSubscription(aVTransportService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'AVTransport' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'AVTransport' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IAVTransportServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IAVTransportServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getAVTransportService()
    {
        return aVTransportService;
    }    


//
// Actions
// =========================================================================
//



    public void pause(PauseInput inp)
    {
        Pause pause = new Pause(aVTransportService, inp, upnpService.getControlPoint());
        pause.executeAction();
    }

    public void stop(StopInput inp)
    {
        Stop stop = new Stop(aVTransportService, inp, upnpService.getControlPoint());
        stop.executeAction();
    }

    public GetPositionInfoOutput getPositionInfo(GetPositionInfoInput inp)
    {
        GetPositionInfo getPositionInfo = new GetPositionInfo(aVTransportService, inp, upnpService.getControlPoint());
        GetPositionInfoOutput res = getPositionInfo.executeAction();
        return res;        
    }

    public void setPlayMode(SetPlayModeInput inp)
    {
        SetPlayMode setPlayMode = new SetPlayMode(aVTransportService, inp, upnpService.getControlPoint());
        setPlayMode.executeAction();
    }

    public void setNextAVTransportURI(SetNextAVTransportURIInput inp)
    {
        SetNextAVTransportURI setNextAVTransportURI = new SetNextAVTransportURI(aVTransportService, inp, upnpService.getControlPoint());
        setNextAVTransportURI.executeAction();
    }

    public void play(PlayInput inp)
    {
        Play play = new Play(aVTransportService, inp, upnpService.getControlPoint());
        play.executeAction();
    }

    public GetDeviceCapabilitiesOutput getDeviceCapabilities(GetDeviceCapabilitiesInput inp)
    {
        GetDeviceCapabilities getDeviceCapabilities = new GetDeviceCapabilities(aVTransportService, inp, upnpService.getControlPoint());
        GetDeviceCapabilitiesOutput res = getDeviceCapabilities.executeAction();
        return res;        
    }

    public GetMediaInfoOutput getMediaInfo(GetMediaInfoInput inp)
    {
        GetMediaInfo getMediaInfo = new GetMediaInfo(aVTransportService, inp, upnpService.getControlPoint());
        GetMediaInfoOutput res = getMediaInfo.executeAction();
        return res;        
    }

    public void next(NextInput inp)
    {
        Next next = new Next(aVTransportService, inp, upnpService.getControlPoint());
        next.executeAction();
    }

    public GetTransportInfoOutput getTransportInfo(GetTransportInfoInput inp)
    {
        GetTransportInfo getTransportInfo = new GetTransportInfo(aVTransportService, inp, upnpService.getControlPoint());
        GetTransportInfoOutput res = getTransportInfo.executeAction();
        return res;        
    }

    public void previous(PreviousInput inp)
    {
        Previous previous = new Previous(aVTransportService, inp, upnpService.getControlPoint());
        previous.executeAction();
    }

    public GetTransportSettingsOutput getTransportSettings(GetTransportSettingsInput inp)
    {
        GetTransportSettings getTransportSettings = new GetTransportSettings(aVTransportService, inp, upnpService.getControlPoint());
        GetTransportSettingsOutput res = getTransportSettings.executeAction();
        return res;        
    }

    public void setAVTransportURI(SetAVTransportURIInput inp)
    {
        SetAVTransportURI setAVTransportURI = new SetAVTransportURI(aVTransportService, inp, upnpService.getControlPoint());
        setAVTransportURI.executeAction();
    }

    public GetCurrentTransportActionsOutput getCurrentTransportActions(GetCurrentTransportActionsInput inp)
    {
        GetCurrentTransportActions getCurrentTransportActions = new GetCurrentTransportActions(aVTransportService, inp, upnpService.getControlPoint());
        GetCurrentTransportActionsOutput res = getCurrentTransportActions.executeAction();
        return res;        
    }

    public void seek(SeekInput inp)
    {
        Seek seek = new Seek(aVTransportService, inp, upnpService.getControlPoint());
        seek.executeAction();
    }
}
