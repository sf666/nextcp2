package nextcp.upnp.modelGen.samsung.com.mainTVAgent21;

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

import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.AddSchedule;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.AddScheduleOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.AddScheduleInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.ChangeSchedule;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.ChangeScheduleOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.ChangeScheduleInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteRecordedItem;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteRecordedItemOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteRecordedItemInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteSchedule;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteScheduleOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteScheduleInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DestoryGroupOwner;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DestoryGroupOwnerOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.EnforceAKE;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.EnforceAKEOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRCurrentChannelName;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRCurrentChannelNameOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRCurrentProgramName;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRCurrentProgramNameOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRMessage;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRMessageOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAPInformation;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAPInformationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAllProgramInformationURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAllProgramInformationURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAllProgramInformationURLInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAvailableActions;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAvailableActionsOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetBannerInformation;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetBannerInformationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetChannelListURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetChannelListURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentBrowserMode;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentBrowserModeOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentBrowserURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentBrowserURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentExternalSource;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentExternalSourceOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentHTSSpeakerLayout;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentHTSSpeakerLayoutOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentMainTVChannel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentMainTVChannelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentProgramInformationURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentProgramInformationURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDTVInformation;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDTVInformationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDetailProgramInformation;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDetailProgramInformationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDetailProgramInformationInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetFilteredProgarmURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetFilteredProgarmURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetFilteredProgarmURLInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSAllSpeakerDistance;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSAllSpeakerDistanceOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSAllSpeakerLevel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSAllSpeakerLevelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSSoundEffect;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSSoundEffectOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSSpeakerConfig;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSSpeakerConfigOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetMBRDeviceList;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetMBRDeviceListOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetMBRDongleStatus;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetMBRDongleStatusOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetRecordChannel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetRecordChannelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetScheduleListURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetScheduleListURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetSourceList;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetSourceListOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.PlayRecordedItem;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.PlayRecordedItemOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.PlayRecordedItemInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.RunBrowser;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.RunBrowserOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.RunBrowserInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendBrowserCommand;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendBrowserCommandOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendBrowserCommandInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendMBRIRKey;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendMBRIRKeyOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendMBRIRKeyInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetAntennaMode;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetAntennaModeOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetAntennaModeInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerDistance;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerDistanceOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerDistanceInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerLevel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerLevelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerLevelInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSSoundEffect;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSSoundEffectOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSSoundEffectInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVChannel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVChannelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVChannelInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVSource;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVSourceOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVSourceInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetRecordDuration;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetRecordDurationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetRecordDurationInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartCloneView;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartCloneViewOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartCloneViewInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartExtSourceView;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartExtSourceViewOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartExtSourceViewInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartInstantRecording;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartInstantRecordingOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartInstantRecordingInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartSecondTVView;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartSecondTVViewOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartSecondTVViewInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopBrowser;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopBrowserOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopRecord;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopRecordOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopRecordInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopView;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopViewOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopViewInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class MainTVAgent2Service
{
    private static Logger log = LoggerFactory.getLogger(MainTVAgent2Service.class.getName());

    private RemoteService mainTVAgent2Service = null;

    private UpnpService upnpService = null;

//    private MainTVAgent2ServiceStateVariable mainTVAgent2ServiceStateVariable = new MainTVAgent2ServiceStateVariable();
    
    private MainTVAgent2ServiceSubscription subscription = null;
    
    public MainTVAgent2Service(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public MainTVAgent2Service(UpnpService upnpService, RemoteDevice device, IMainTVAgent2ServiceEventListener listener)
    {
        this.upnpService = upnpService;
        mainTVAgent2Service = device.findService(new ServiceType("samsung.com", "MainTVAgent2"));
        if (mainTVAgent2Service != null)
        {
	        subscription = new MainTVAgent2ServiceSubscription(mainTVAgent2Service, 600);
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
	
	        log.info(String.format("initialized service 'MainTVAgent2' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'MainTVAgent2' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IMainTVAgent2ServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IMainTVAgent2ServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getMainTVAgent2Service()
    {
        return mainTVAgent2Service;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return mainTVAgent2Service != null && mainTVAgent2Service.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public AddScheduleOutput addSchedule(AddScheduleInput inp)
    {
        if (!hasAction("AddSchedule"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action AddSchedule of service MainTVAgent2");
        }
        AddSchedule addSchedule = new AddSchedule(mainTVAgent2Service, inp, upnpService.getControlPoint());
        AddScheduleOutput res = addSchedule.executeAction();
        return res;        
    }

    public ChangeScheduleOutput changeSchedule(ChangeScheduleInput inp)
    {
        if (!hasAction("ChangeSchedule"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ChangeSchedule of service MainTVAgent2");
        }
        ChangeSchedule changeSchedule = new ChangeSchedule(mainTVAgent2Service, inp, upnpService.getControlPoint());
        ChangeScheduleOutput res = changeSchedule.executeAction();
        return res;        
    }

    public DeleteRecordedItemOutput deleteRecordedItem(DeleteRecordedItemInput inp)
    {
        if (!hasAction("DeleteRecordedItem"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action DeleteRecordedItem of service MainTVAgent2");
        }
        DeleteRecordedItem deleteRecordedItem = new DeleteRecordedItem(mainTVAgent2Service, inp, upnpService.getControlPoint());
        DeleteRecordedItemOutput res = deleteRecordedItem.executeAction();
        return res;        
    }

    public DeleteScheduleOutput deleteSchedule(DeleteScheduleInput inp)
    {
        if (!hasAction("DeleteSchedule"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action DeleteSchedule of service MainTVAgent2");
        }
        DeleteSchedule deleteSchedule = new DeleteSchedule(mainTVAgent2Service, inp, upnpService.getControlPoint());
        DeleteScheduleOutput res = deleteSchedule.executeAction();
        return res;        
    }

    public DestoryGroupOwnerOutput destoryGroupOwner()
    {
        if (!hasAction("DestoryGroupOwner"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action DestoryGroupOwner of service MainTVAgent2");
        }
        DestoryGroupOwner destoryGroupOwner = new DestoryGroupOwner(mainTVAgent2Service,  upnpService.getControlPoint());
        DestoryGroupOwnerOutput res = destoryGroupOwner.executeAction();
        return res;        
    }

    public EnforceAKEOutput enforceAKE()
    {
        if (!hasAction("EnforceAKE"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action EnforceAKE of service MainTVAgent2");
        }
        EnforceAKE enforceAKE = new EnforceAKE(mainTVAgent2Service,  upnpService.getControlPoint());
        EnforceAKEOutput res = enforceAKE.executeAction();
        return res;        
    }

    public GetACRCurrentChannelNameOutput getACRCurrentChannelName()
    {
        if (!hasAction("GetACRCurrentChannelName"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetACRCurrentChannelName of service MainTVAgent2");
        }
        GetACRCurrentChannelName getACRCurrentChannelName = new GetACRCurrentChannelName(mainTVAgent2Service,  upnpService.getControlPoint());
        GetACRCurrentChannelNameOutput res = getACRCurrentChannelName.executeAction();
        return res;        
    }

    public GetACRCurrentProgramNameOutput getACRCurrentProgramName()
    {
        if (!hasAction("GetACRCurrentProgramName"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetACRCurrentProgramName of service MainTVAgent2");
        }
        GetACRCurrentProgramName getACRCurrentProgramName = new GetACRCurrentProgramName(mainTVAgent2Service,  upnpService.getControlPoint());
        GetACRCurrentProgramNameOutput res = getACRCurrentProgramName.executeAction();
        return res;        
    }

    public GetACRMessageOutput getACRMessage()
    {
        if (!hasAction("GetACRMessage"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetACRMessage of service MainTVAgent2");
        }
        GetACRMessage getACRMessage = new GetACRMessage(mainTVAgent2Service,  upnpService.getControlPoint());
        GetACRMessageOutput res = getACRMessage.executeAction();
        return res;        
    }

    public GetAPInformationOutput getAPInformation()
    {
        if (!hasAction("GetAPInformation"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAPInformation of service MainTVAgent2");
        }
        GetAPInformation getAPInformation = new GetAPInformation(mainTVAgent2Service,  upnpService.getControlPoint());
        GetAPInformationOutput res = getAPInformation.executeAction();
        return res;        
    }

    public GetAllProgramInformationURLOutput getAllProgramInformationURL(GetAllProgramInformationURLInput inp)
    {
        if (!hasAction("GetAllProgramInformationURL"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAllProgramInformationURL of service MainTVAgent2");
        }
        GetAllProgramInformationURL getAllProgramInformationURL = new GetAllProgramInformationURL(mainTVAgent2Service, inp, upnpService.getControlPoint());
        GetAllProgramInformationURLOutput res = getAllProgramInformationURL.executeAction();
        return res;        
    }

    public GetAvailableActionsOutput getAvailableActions()
    {
        if (!hasAction("GetAvailableActions"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAvailableActions of service MainTVAgent2");
        }
        GetAvailableActions getAvailableActions = new GetAvailableActions(mainTVAgent2Service,  upnpService.getControlPoint());
        GetAvailableActionsOutput res = getAvailableActions.executeAction();
        return res;        
    }

    public GetBannerInformationOutput getBannerInformation()
    {
        if (!hasAction("GetBannerInformation"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetBannerInformation of service MainTVAgent2");
        }
        GetBannerInformation getBannerInformation = new GetBannerInformation(mainTVAgent2Service,  upnpService.getControlPoint());
        GetBannerInformationOutput res = getBannerInformation.executeAction();
        return res;        
    }

    public GetChannelListURLOutput getChannelListURL()
    {
        if (!hasAction("GetChannelListURL"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetChannelListURL of service MainTVAgent2");
        }
        GetChannelListURL getChannelListURL = new GetChannelListURL(mainTVAgent2Service,  upnpService.getControlPoint());
        GetChannelListURLOutput res = getChannelListURL.executeAction();
        return res;        
    }

    public GetCurrentBrowserModeOutput getCurrentBrowserMode()
    {
        if (!hasAction("GetCurrentBrowserMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetCurrentBrowserMode of service MainTVAgent2");
        }
        GetCurrentBrowserMode getCurrentBrowserMode = new GetCurrentBrowserMode(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentBrowserModeOutput res = getCurrentBrowserMode.executeAction();
        return res;        
    }

    public GetCurrentBrowserURLOutput getCurrentBrowserURL()
    {
        if (!hasAction("GetCurrentBrowserURL"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetCurrentBrowserURL of service MainTVAgent2");
        }
        GetCurrentBrowserURL getCurrentBrowserURL = new GetCurrentBrowserURL(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentBrowserURLOutput res = getCurrentBrowserURL.executeAction();
        return res;        
    }

    public GetCurrentExternalSourceOutput getCurrentExternalSource()
    {
        if (!hasAction("GetCurrentExternalSource"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetCurrentExternalSource of service MainTVAgent2");
        }
        GetCurrentExternalSource getCurrentExternalSource = new GetCurrentExternalSource(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentExternalSourceOutput res = getCurrentExternalSource.executeAction();
        return res;        
    }

    public GetCurrentHTSSpeakerLayoutOutput getCurrentHTSSpeakerLayout()
    {
        if (!hasAction("GetCurrentHTSSpeakerLayout"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetCurrentHTSSpeakerLayout of service MainTVAgent2");
        }
        GetCurrentHTSSpeakerLayout getCurrentHTSSpeakerLayout = new GetCurrentHTSSpeakerLayout(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentHTSSpeakerLayoutOutput res = getCurrentHTSSpeakerLayout.executeAction();
        return res;        
    }

    public GetCurrentMainTVChannelOutput getCurrentMainTVChannel()
    {
        if (!hasAction("GetCurrentMainTVChannel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetCurrentMainTVChannel of service MainTVAgent2");
        }
        GetCurrentMainTVChannel getCurrentMainTVChannel = new GetCurrentMainTVChannel(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentMainTVChannelOutput res = getCurrentMainTVChannel.executeAction();
        return res;        
    }

    public GetCurrentProgramInformationURLOutput getCurrentProgramInformationURL()
    {
        if (!hasAction("GetCurrentProgramInformationURL"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetCurrentProgramInformationURL of service MainTVAgent2");
        }
        GetCurrentProgramInformationURL getCurrentProgramInformationURL = new GetCurrentProgramInformationURL(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentProgramInformationURLOutput res = getCurrentProgramInformationURL.executeAction();
        return res;        
    }

    public GetDTVInformationOutput getDTVInformation()
    {
        if (!hasAction("GetDTVInformation"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDTVInformation of service MainTVAgent2");
        }
        GetDTVInformation getDTVInformation = new GetDTVInformation(mainTVAgent2Service,  upnpService.getControlPoint());
        GetDTVInformationOutput res = getDTVInformation.executeAction();
        return res;        
    }

    public GetDetailProgramInformationOutput getDetailProgramInformation(GetDetailProgramInformationInput inp)
    {
        if (!hasAction("GetDetailProgramInformation"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDetailProgramInformation of service MainTVAgent2");
        }
        GetDetailProgramInformation getDetailProgramInformation = new GetDetailProgramInformation(mainTVAgent2Service, inp, upnpService.getControlPoint());
        GetDetailProgramInformationOutput res = getDetailProgramInformation.executeAction();
        return res;        
    }

    public GetFilteredProgarmURLOutput getFilteredProgarmURL(GetFilteredProgarmURLInput inp)
    {
        if (!hasAction("GetFilteredProgarmURL"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetFilteredProgarmURL of service MainTVAgent2");
        }
        GetFilteredProgarmURL getFilteredProgarmURL = new GetFilteredProgarmURL(mainTVAgent2Service, inp, upnpService.getControlPoint());
        GetFilteredProgarmURLOutput res = getFilteredProgarmURL.executeAction();
        return res;        
    }

    public GetHTSAllSpeakerDistanceOutput getHTSAllSpeakerDistance()
    {
        if (!hasAction("GetHTSAllSpeakerDistance"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetHTSAllSpeakerDistance of service MainTVAgent2");
        }
        GetHTSAllSpeakerDistance getHTSAllSpeakerDistance = new GetHTSAllSpeakerDistance(mainTVAgent2Service,  upnpService.getControlPoint());
        GetHTSAllSpeakerDistanceOutput res = getHTSAllSpeakerDistance.executeAction();
        return res;        
    }

    public GetHTSAllSpeakerLevelOutput getHTSAllSpeakerLevel()
    {
        if (!hasAction("GetHTSAllSpeakerLevel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetHTSAllSpeakerLevel of service MainTVAgent2");
        }
        GetHTSAllSpeakerLevel getHTSAllSpeakerLevel = new GetHTSAllSpeakerLevel(mainTVAgent2Service,  upnpService.getControlPoint());
        GetHTSAllSpeakerLevelOutput res = getHTSAllSpeakerLevel.executeAction();
        return res;        
    }

    public GetHTSSoundEffectOutput getHTSSoundEffect()
    {
        if (!hasAction("GetHTSSoundEffect"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetHTSSoundEffect of service MainTVAgent2");
        }
        GetHTSSoundEffect getHTSSoundEffect = new GetHTSSoundEffect(mainTVAgent2Service,  upnpService.getControlPoint());
        GetHTSSoundEffectOutput res = getHTSSoundEffect.executeAction();
        return res;        
    }

    public GetHTSSpeakerConfigOutput getHTSSpeakerConfig()
    {
        if (!hasAction("GetHTSSpeakerConfig"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetHTSSpeakerConfig of service MainTVAgent2");
        }
        GetHTSSpeakerConfig getHTSSpeakerConfig = new GetHTSSpeakerConfig(mainTVAgent2Service,  upnpService.getControlPoint());
        GetHTSSpeakerConfigOutput res = getHTSSpeakerConfig.executeAction();
        return res;        
    }

    public GetMBRDeviceListOutput getMBRDeviceList()
    {
        if (!hasAction("GetMBRDeviceList"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMBRDeviceList of service MainTVAgent2");
        }
        GetMBRDeviceList getMBRDeviceList = new GetMBRDeviceList(mainTVAgent2Service,  upnpService.getControlPoint());
        GetMBRDeviceListOutput res = getMBRDeviceList.executeAction();
        return res;        
    }

    public GetMBRDongleStatusOutput getMBRDongleStatus()
    {
        if (!hasAction("GetMBRDongleStatus"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMBRDongleStatus of service MainTVAgent2");
        }
        GetMBRDongleStatus getMBRDongleStatus = new GetMBRDongleStatus(mainTVAgent2Service,  upnpService.getControlPoint());
        GetMBRDongleStatusOutput res = getMBRDongleStatus.executeAction();
        return res;        
    }

    public GetRecordChannelOutput getRecordChannel()
    {
        if (!hasAction("GetRecordChannel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetRecordChannel of service MainTVAgent2");
        }
        GetRecordChannel getRecordChannel = new GetRecordChannel(mainTVAgent2Service,  upnpService.getControlPoint());
        GetRecordChannelOutput res = getRecordChannel.executeAction();
        return res;        
    }

    public GetScheduleListURLOutput getScheduleListURL()
    {
        if (!hasAction("GetScheduleListURL"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetScheduleListURL of service MainTVAgent2");
        }
        GetScheduleListURL getScheduleListURL = new GetScheduleListURL(mainTVAgent2Service,  upnpService.getControlPoint());
        GetScheduleListURLOutput res = getScheduleListURL.executeAction();
        return res;        
    }

    public GetSourceListOutput getSourceList()
    {
        if (!hasAction("GetSourceList"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSourceList of service MainTVAgent2");
        }
        GetSourceList getSourceList = new GetSourceList(mainTVAgent2Service,  upnpService.getControlPoint());
        GetSourceListOutput res = getSourceList.executeAction();
        return res;        
    }

    public PlayRecordedItemOutput playRecordedItem(PlayRecordedItemInput inp)
    {
        if (!hasAction("PlayRecordedItem"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action PlayRecordedItem of service MainTVAgent2");
        }
        PlayRecordedItem playRecordedItem = new PlayRecordedItem(mainTVAgent2Service, inp, upnpService.getControlPoint());
        PlayRecordedItemOutput res = playRecordedItem.executeAction();
        return res;        
    }

    public RunBrowserOutput runBrowser(RunBrowserInput inp)
    {
        if (!hasAction("RunBrowser"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action RunBrowser of service MainTVAgent2");
        }
        RunBrowser runBrowser = new RunBrowser(mainTVAgent2Service, inp, upnpService.getControlPoint());
        RunBrowserOutput res = runBrowser.executeAction();
        return res;        
    }

    public SendBrowserCommandOutput sendBrowserCommand(SendBrowserCommandInput inp)
    {
        if (!hasAction("SendBrowserCommand"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SendBrowserCommand of service MainTVAgent2");
        }
        SendBrowserCommand sendBrowserCommand = new SendBrowserCommand(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SendBrowserCommandOutput res = sendBrowserCommand.executeAction();
        return res;        
    }

    public SendMBRIRKeyOutput sendMBRIRKey(SendMBRIRKeyInput inp)
    {
        if (!hasAction("SendMBRIRKey"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SendMBRIRKey of service MainTVAgent2");
        }
        SendMBRIRKey sendMBRIRKey = new SendMBRIRKey(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SendMBRIRKeyOutput res = sendMBRIRKey.executeAction();
        return res;        
    }

    public SetAntennaModeOutput setAntennaMode(SetAntennaModeInput inp)
    {
        if (!hasAction("SetAntennaMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAntennaMode of service MainTVAgent2");
        }
        SetAntennaMode setAntennaMode = new SetAntennaMode(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetAntennaModeOutput res = setAntennaMode.executeAction();
        return res;        
    }

    public SetHTSAllSpeakerDistanceOutput setHTSAllSpeakerDistance(SetHTSAllSpeakerDistanceInput inp)
    {
        if (!hasAction("SetHTSAllSpeakerDistance"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetHTSAllSpeakerDistance of service MainTVAgent2");
        }
        SetHTSAllSpeakerDistance setHTSAllSpeakerDistance = new SetHTSAllSpeakerDistance(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetHTSAllSpeakerDistanceOutput res = setHTSAllSpeakerDistance.executeAction();
        return res;        
    }

    public SetHTSAllSpeakerLevelOutput setHTSAllSpeakerLevel(SetHTSAllSpeakerLevelInput inp)
    {
        if (!hasAction("SetHTSAllSpeakerLevel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetHTSAllSpeakerLevel of service MainTVAgent2");
        }
        SetHTSAllSpeakerLevel setHTSAllSpeakerLevel = new SetHTSAllSpeakerLevel(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetHTSAllSpeakerLevelOutput res = setHTSAllSpeakerLevel.executeAction();
        return res;        
    }

    public SetHTSSoundEffectOutput setHTSSoundEffect(SetHTSSoundEffectInput inp)
    {
        if (!hasAction("SetHTSSoundEffect"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetHTSSoundEffect of service MainTVAgent2");
        }
        SetHTSSoundEffect setHTSSoundEffect = new SetHTSSoundEffect(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetHTSSoundEffectOutput res = setHTSSoundEffect.executeAction();
        return res;        
    }

    public SetMainTVChannelOutput setMainTVChannel(SetMainTVChannelInput inp)
    {
        if (!hasAction("SetMainTVChannel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetMainTVChannel of service MainTVAgent2");
        }
        SetMainTVChannel setMainTVChannel = new SetMainTVChannel(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetMainTVChannelOutput res = setMainTVChannel.executeAction();
        return res;        
    }

    public SetMainTVSourceOutput setMainTVSource(SetMainTVSourceInput inp)
    {
        if (!hasAction("SetMainTVSource"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetMainTVSource of service MainTVAgent2");
        }
        SetMainTVSource setMainTVSource = new SetMainTVSource(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetMainTVSourceOutput res = setMainTVSource.executeAction();
        return res;        
    }

    public SetRecordDurationOutput setRecordDuration(SetRecordDurationInput inp)
    {
        if (!hasAction("SetRecordDuration"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetRecordDuration of service MainTVAgent2");
        }
        SetRecordDuration setRecordDuration = new SetRecordDuration(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetRecordDurationOutput res = setRecordDuration.executeAction();
        return res;        
    }

    public StartCloneViewOutput startCloneView(StartCloneViewInput inp)
    {
        if (!hasAction("StartCloneView"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StartCloneView of service MainTVAgent2");
        }
        StartCloneView startCloneView = new StartCloneView(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StartCloneViewOutput res = startCloneView.executeAction();
        return res;        
    }

    public StartExtSourceViewOutput startExtSourceView(StartExtSourceViewInput inp)
    {
        if (!hasAction("StartExtSourceView"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StartExtSourceView of service MainTVAgent2");
        }
        StartExtSourceView startExtSourceView = new StartExtSourceView(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StartExtSourceViewOutput res = startExtSourceView.executeAction();
        return res;        
    }

    public StartInstantRecordingOutput startInstantRecording(StartInstantRecordingInput inp)
    {
        if (!hasAction("StartInstantRecording"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StartInstantRecording of service MainTVAgent2");
        }
        StartInstantRecording startInstantRecording = new StartInstantRecording(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StartInstantRecordingOutput res = startInstantRecording.executeAction();
        return res;        
    }

    public StartSecondTVViewOutput startSecondTVView(StartSecondTVViewInput inp)
    {
        if (!hasAction("StartSecondTVView"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StartSecondTVView of service MainTVAgent2");
        }
        StartSecondTVView startSecondTVView = new StartSecondTVView(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StartSecondTVViewOutput res = startSecondTVView.executeAction();
        return res;        
    }

    public StopBrowserOutput stopBrowser()
    {
        if (!hasAction("StopBrowser"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StopBrowser of service MainTVAgent2");
        }
        StopBrowser stopBrowser = new StopBrowser(mainTVAgent2Service,  upnpService.getControlPoint());
        StopBrowserOutput res = stopBrowser.executeAction();
        return res;        
    }

    public StopRecordOutput stopRecord(StopRecordInput inp)
    {
        if (!hasAction("StopRecord"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StopRecord of service MainTVAgent2");
        }
        StopRecord stopRecord = new StopRecord(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StopRecordOutput res = stopRecord.executeAction();
        return res;        
    }

    public StopViewOutput stopView(StopViewInput inp)
    {
        if (!hasAction("StopView"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StopView of service MainTVAgent2");
        }
        StopView stopView = new StopView(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StopViewOutput res = stopView.executeAction();
        return res;        
    }
}
