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

import nextcp.upnp.GenActionException;
import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetCurrentTransportActions;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetCurrentTransportActionsOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetCurrentTransportActionsInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDRMState;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDRMStateOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDRMStateInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDeviceCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDeviceCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetDeviceCapabilitiesInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfo_Ext;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfo_ExtOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetMediaInfo_ExtInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetPositionInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetPositionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetPositionInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportSettings;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportSettingsOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.GetTransportSettingsInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Next;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.NextInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Pause;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.PauseInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Play;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.PlayInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Previous;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.PreviousInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Seek;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SeekInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetAVTransportURI;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetNextAVTransportURI;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetNextAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetPlayMode;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.SetPlayModeInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.Stop;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.StopInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.X_DLNA_GetBytePositionInfo;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.X_DLNA_GetBytePositionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport2.actions.X_DLNA_GetBytePositionInfoInput;


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
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public AVTransportService(UpnpService upnpService, RemoteDevice device, IAVTransportServiceEventListener listener)
    {
        this.upnpService = upnpService;
        aVTransportService = device.findService(new ServiceType("schemas-upnp-org", "AVTransport"));
        if (aVTransportService != null)
        {
	        subscription = new AVTransportServiceSubscription(aVTransportService, 600);
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

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return aVTransportService != null && aVTransportService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public GetCurrentTransportActionsOutput getCurrentTransportActions(GetCurrentTransportActionsInput inp)
    {
        if (!hasAction("GetCurrentTransportActions"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetCurrentTransportActions of service AVTransport");
        }
        GetCurrentTransportActions getCurrentTransportActions = new GetCurrentTransportActions(aVTransportService, inp, upnpService.getControlPoint());
        GetCurrentTransportActionsOutput res = getCurrentTransportActions.executeAction();
        return res;        
    }

    public GetDRMStateOutput getDRMState(GetDRMStateInput inp)
    {
        if (!hasAction("GetDRMState"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDRMState of service AVTransport");
        }
        GetDRMState getDRMState = new GetDRMState(aVTransportService, inp, upnpService.getControlPoint());
        GetDRMStateOutput res = getDRMState.executeAction();
        return res;        
    }

    public GetDeviceCapabilitiesOutput getDeviceCapabilities(GetDeviceCapabilitiesInput inp)
    {
        if (!hasAction("GetDeviceCapabilities"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDeviceCapabilities of service AVTransport");
        }
        GetDeviceCapabilities getDeviceCapabilities = new GetDeviceCapabilities(aVTransportService, inp, upnpService.getControlPoint());
        GetDeviceCapabilitiesOutput res = getDeviceCapabilities.executeAction();
        return res;        
    }

    public GetMediaInfoOutput getMediaInfo(GetMediaInfoInput inp)
    {
        if (!hasAction("GetMediaInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMediaInfo of service AVTransport");
        }
        GetMediaInfo getMediaInfo = new GetMediaInfo(aVTransportService, inp, upnpService.getControlPoint());
        GetMediaInfoOutput res = getMediaInfo.executeAction();
        return res;        
    }

    public GetMediaInfo_ExtOutput getMediaInfo_Ext(GetMediaInfo_ExtInput inp)
    {
        if (!hasAction("GetMediaInfo_Ext"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMediaInfo_Ext of service AVTransport");
        }
        GetMediaInfo_Ext getMediaInfo_Ext = new GetMediaInfo_Ext(aVTransportService, inp, upnpService.getControlPoint());
        GetMediaInfo_ExtOutput res = getMediaInfo_Ext.executeAction();
        return res;        
    }

    public GetPositionInfoOutput getPositionInfo(GetPositionInfoInput inp)
    {
        if (!hasAction("GetPositionInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPositionInfo of service AVTransport");
        }
        GetPositionInfo getPositionInfo = new GetPositionInfo(aVTransportService, inp, upnpService.getControlPoint());
        GetPositionInfoOutput res = getPositionInfo.executeAction();
        return res;        
    }

    public GetTransportInfoOutput getTransportInfo(GetTransportInfoInput inp)
    {
        if (!hasAction("GetTransportInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetTransportInfo of service AVTransport");
        }
        GetTransportInfo getTransportInfo = new GetTransportInfo(aVTransportService, inp, upnpService.getControlPoint());
        GetTransportInfoOutput res = getTransportInfo.executeAction();
        return res;        
    }

    public GetTransportSettingsOutput getTransportSettings(GetTransportSettingsInput inp)
    {
        if (!hasAction("GetTransportSettings"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetTransportSettings of service AVTransport");
        }
        GetTransportSettings getTransportSettings = new GetTransportSettings(aVTransportService, inp, upnpService.getControlPoint());
        GetTransportSettingsOutput res = getTransportSettings.executeAction();
        return res;        
    }

    public void next(NextInput inp)
    {
        if (!hasAction("Next"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Next of service AVTransport");
        }
        Next next = new Next(aVTransportService, inp, upnpService.getControlPoint());
        next.executeAction();
    }

    public void pause(PauseInput inp)
    {
        if (!hasAction("Pause"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Pause of service AVTransport");
        }
        Pause pause = new Pause(aVTransportService, inp, upnpService.getControlPoint());
        pause.executeAction();
    }

    public void play(PlayInput inp)
    {
        if (!hasAction("Play"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Play of service AVTransport");
        }
        Play play = new Play(aVTransportService, inp, upnpService.getControlPoint());
        play.executeAction();
    }

    public void previous(PreviousInput inp)
    {
        if (!hasAction("Previous"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Previous of service AVTransport");
        }
        Previous previous = new Previous(aVTransportService, inp, upnpService.getControlPoint());
        previous.executeAction();
    }

    public void seek(SeekInput inp)
    {
        if (!hasAction("Seek"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Seek of service AVTransport");
        }
        Seek seek = new Seek(aVTransportService, inp, upnpService.getControlPoint());
        seek.executeAction();
    }

    public void setAVTransportURI(SetAVTransportURIInput inp)
    {
        if (!hasAction("SetAVTransportURI"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAVTransportURI of service AVTransport");
        }
        SetAVTransportURI setAVTransportURI = new SetAVTransportURI(aVTransportService, inp, upnpService.getControlPoint());
        setAVTransportURI.executeAction();
    }

    public void setNextAVTransportURI(SetNextAVTransportURIInput inp)
    {
        if (!hasAction("SetNextAVTransportURI"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetNextAVTransportURI of service AVTransport");
        }
        SetNextAVTransportURI setNextAVTransportURI = new SetNextAVTransportURI(aVTransportService, inp, upnpService.getControlPoint());
        setNextAVTransportURI.executeAction();
    }

    public void setPlayMode(SetPlayModeInput inp)
    {
        if (!hasAction("SetPlayMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetPlayMode of service AVTransport");
        }
        SetPlayMode setPlayMode = new SetPlayMode(aVTransportService, inp, upnpService.getControlPoint());
        setPlayMode.executeAction();
    }

    public void stop(StopInput inp)
    {
        if (!hasAction("Stop"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Stop of service AVTransport");
        }
        Stop stop = new Stop(aVTransportService, inp, upnpService.getControlPoint());
        stop.executeAction();
    }

    public X_DLNA_GetBytePositionInfoOutput x_DLNA_GetBytePositionInfo(X_DLNA_GetBytePositionInfoInput inp)
    {
        if (!hasAction("X_DLNA_GetBytePositionInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_DLNA_GetBytePositionInfo of service AVTransport");
        }
        X_DLNA_GetBytePositionInfo x_DLNA_GetBytePositionInfo = new X_DLNA_GetBytePositionInfo(aVTransportService, inp, upnpService.getControlPoint());
        X_DLNA_GetBytePositionInfoOutput res = x_DLNA_GetBytePositionInfo.executeAction();
        return res;        
    }
}
