package nextcp.upnp.modelGen.schemasupnporg.aVTransport;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.Pause;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.PauseInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.Stop;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.StopInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetPositionInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetPositionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetPositionInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetPlayMode;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetPlayModeInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetNextAVTransportURI;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetNextAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.Play;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.PlayInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetDeviceCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetDeviceCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetDeviceCapabilitiesInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetMediaInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetMediaInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetMediaInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.Next;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.NextInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetTransportInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetTransportInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetTransportInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.Previous;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.PreviousInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetTransportSettings;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetTransportSettingsOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetTransportSettingsInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetAVTransportURI;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetCurrentTransportActions;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetCurrentTransportActionsOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetCurrentTransportActionsInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.Seek;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SeekInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
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
    
    public void addSubscriptionEventListener(IAVTransportServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IAVTransportServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getAVTransportService()
    {
        return aVTransportService;
    }    


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
