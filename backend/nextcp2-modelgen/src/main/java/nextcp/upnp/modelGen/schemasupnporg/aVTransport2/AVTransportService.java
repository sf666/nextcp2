package nextcp.upnp.modelGen.schemasupnporg.aVTransport2;

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

import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Pause;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.PauseInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Stop;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.StopInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetPositionInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetPositionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetPositionInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDRMState;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDRMStateOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDRMStateInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetNextAVTransportURI;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetNextAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Play;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.PlayInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDeviceCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDeviceCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDeviceCapabilitiesInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Next;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.NextInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Previous;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.PreviousInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetAVTransportURI;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportSettings;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportSettingsOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportSettingsInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfo_Ext;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfo_ExtOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfo_ExtInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Seek;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SeekInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetCurrentTransportActions;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetCurrentTransportActionsOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetCurrentTransportActionsInput;


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

//    private AVTransportServiceStateVariable aVTransportServiceStateVariable = new AVTransportServiceStateVariable();
    
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

    public GetDRMStateOutput getDRMState(GetDRMStateInput inp)
    {
        GetDRMState getDRMState = new GetDRMState(aVTransportService, inp, upnpService.getControlPoint());
        GetDRMStateOutput res = getDRMState.executeAction();
        return res;        
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

    public void setAVTransportURI(SetAVTransportURIInput inp)
    {
        SetAVTransportURI setAVTransportURI = new SetAVTransportURI(aVTransportService, inp, upnpService.getControlPoint());
        setAVTransportURI.executeAction();
    }

    public GetTransportSettingsOutput getTransportSettings(GetTransportSettingsInput inp)
    {
        GetTransportSettings getTransportSettings = new GetTransportSettings(aVTransportService, inp, upnpService.getControlPoint());
        GetTransportSettingsOutput res = getTransportSettings.executeAction();
        return res;        
    }

    public GetMediaInfo_ExtOutput getMediaInfo_Ext(GetMediaInfo_ExtInput inp)
    {
        GetMediaInfo_Ext getMediaInfo_Ext = new GetMediaInfo_Ext(aVTransportService, inp, upnpService.getControlPoint());
        GetMediaInfo_ExtOutput res = getMediaInfo_Ext.executeAction();
        return res;        
    }

    public void seek(SeekInput inp)
    {
        Seek seek = new Seek(aVTransportService, inp, upnpService.getControlPoint());
        seek.executeAction();
    }

    public GetCurrentTransportActionsOutput getCurrentTransportActions(GetCurrentTransportActionsInput inp)
    {
        GetCurrentTransportActions getCurrentTransportActions = new GetCurrentTransportActions(aVTransportService, inp, upnpService.getControlPoint());
        GetCurrentTransportActionsOutput res = getCurrentTransportActions.executeAction();
        return res;        
    }
}
