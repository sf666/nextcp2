package nextcp.upnp.modelGen.samsung.com.mainTVAgent21;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetMBRDeviceList;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetMBRDeviceListOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.AddSchedule;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.AddScheduleOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.AddScheduleInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVSource;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVSourceOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVSourceInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAPInformation;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAPInformationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSAllSpeakerLevel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSAllSpeakerLevelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentMainTVChannel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentMainTVChannelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentProgramInformationURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentProgramInformationURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetAntennaMode;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetAntennaModeOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetAntennaModeInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSAllSpeakerDistance;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSAllSpeakerDistanceOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartInstantRecording;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartInstantRecordingOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartInstantRecordingInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DestoryGroupOwner;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DestoryGroupOwnerOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendMBRIRKey;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendMBRIRKeyOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendMBRIRKeyInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerLevel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerLevelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerLevelInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVChannel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVChannelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetMainTVChannelInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetBannerInformation;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetBannerInformationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.ChangeSchedule;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.ChangeScheduleOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.ChangeScheduleInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.EnforceAKE;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.EnforceAKEOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetSourceList;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetSourceListOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartExtSourceView;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartExtSourceViewOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartExtSourceViewInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteRecordedItem;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteRecordedItemOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteRecordedItemInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetScheduleListURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetScheduleListURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSSpeakerConfig;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSSpeakerConfigOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetChannelListURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetChannelListURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopRecord;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopRecordOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopRecordInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteSchedule;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteScheduleOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.DeleteScheduleInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRMessage;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRMessageOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerDistance;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerDistanceOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSAllSpeakerDistanceInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetRecordChannel;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetRecordChannelOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.PlayRecordedItem;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.PlayRecordedItemOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.PlayRecordedItemInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetRecordDuration;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetRecordDurationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetRecordDurationInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDTVInformation;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDTVInformationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRCurrentProgramName;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRCurrentProgramNameOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartCloneView;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartCloneViewOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartCloneViewInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartSecondTVView;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartSecondTVViewOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StartSecondTVViewInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSSoundEffect;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetHTSSoundEffectOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAllProgramInformationURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAllProgramInformationURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAllProgramInformationURLInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAvailableActions;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetAvailableActionsOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRCurrentChannelName;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetACRCurrentChannelNameOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentBrowserURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentBrowserURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetFilteredProgarmURL;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetFilteredProgarmURLOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetFilteredProgarmURLInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopBrowser;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopBrowserOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentExternalSource;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentExternalSourceOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopView;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopViewOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.StopViewInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSSoundEffect;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSSoundEffectOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SetHTSSoundEffectInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDetailProgramInformation;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDetailProgramInformationOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetDetailProgramInformationInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetMBRDongleStatus;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetMBRDongleStatusOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendBrowserCommand;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendBrowserCommandOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.SendBrowserCommandInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.RunBrowser;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.RunBrowserOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.RunBrowserInput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentBrowserMode;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentBrowserModeOutput;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentHTSSpeakerLayout;
import nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions.GetCurrentHTSSpeakerLayoutOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class MainTVAgent2Service
{
    private static Logger log = LoggerFactory.getLogger(MainTVAgent2Service.class.getName());

    private RemoteService mainTVAgent2Service = null;

    private UpnpService upnpService = null;

    // private MainTVAgent2ServiceStateVariable mainTVAgent2ServiceStateVariable = new MainTVAgent2ServiceStateVariable();
    
    private MainTVAgent2ServiceSubscription subscription = null;
    
    public MainTVAgent2Service(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        mainTVAgent2Service = device.findService(new ServiceType("samsung.com", "MainTVAgent2"));
        if (mainTVAgent2Service != null)
        {
	        subscription = new MainTVAgent2ServiceSubscription(mainTVAgent2Service, 600);
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
    
    public void addSubscriptionEventListener(IMainTVAgent2ServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IMainTVAgent2ServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getMainTVAgent2Service()
    {
        return mainTVAgent2Service;
    }    


    public GetMBRDeviceListOutput getMBRDeviceList()
    {
        GetMBRDeviceList getMBRDeviceList = new GetMBRDeviceList(mainTVAgent2Service,  upnpService.getControlPoint());
        GetMBRDeviceListOutput res = getMBRDeviceList.executeAction();
        return res;        
    }

    public AddScheduleOutput addSchedule(AddScheduleInput inp)
    {
        AddSchedule addSchedule = new AddSchedule(mainTVAgent2Service, inp, upnpService.getControlPoint());
        AddScheduleOutput res = addSchedule.executeAction();
        return res;        
    }

    public SetMainTVSourceOutput setMainTVSource(SetMainTVSourceInput inp)
    {
        SetMainTVSource setMainTVSource = new SetMainTVSource(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetMainTVSourceOutput res = setMainTVSource.executeAction();
        return res;        
    }

    public GetAPInformationOutput getAPInformation()
    {
        GetAPInformation getAPInformation = new GetAPInformation(mainTVAgent2Service,  upnpService.getControlPoint());
        GetAPInformationOutput res = getAPInformation.executeAction();
        return res;        
    }

    public GetHTSAllSpeakerLevelOutput getHTSAllSpeakerLevel()
    {
        GetHTSAllSpeakerLevel getHTSAllSpeakerLevel = new GetHTSAllSpeakerLevel(mainTVAgent2Service,  upnpService.getControlPoint());
        GetHTSAllSpeakerLevelOutput res = getHTSAllSpeakerLevel.executeAction();
        return res;        
    }

    public GetCurrentMainTVChannelOutput getCurrentMainTVChannel()
    {
        GetCurrentMainTVChannel getCurrentMainTVChannel = new GetCurrentMainTVChannel(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentMainTVChannelOutput res = getCurrentMainTVChannel.executeAction();
        return res;        
    }

    public GetCurrentProgramInformationURLOutput getCurrentProgramInformationURL()
    {
        GetCurrentProgramInformationURL getCurrentProgramInformationURL = new GetCurrentProgramInformationURL(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentProgramInformationURLOutput res = getCurrentProgramInformationURL.executeAction();
        return res;        
    }

    public SetAntennaModeOutput setAntennaMode(SetAntennaModeInput inp)
    {
        SetAntennaMode setAntennaMode = new SetAntennaMode(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetAntennaModeOutput res = setAntennaMode.executeAction();
        return res;        
    }

    public GetHTSAllSpeakerDistanceOutput getHTSAllSpeakerDistance()
    {
        GetHTSAllSpeakerDistance getHTSAllSpeakerDistance = new GetHTSAllSpeakerDistance(mainTVAgent2Service,  upnpService.getControlPoint());
        GetHTSAllSpeakerDistanceOutput res = getHTSAllSpeakerDistance.executeAction();
        return res;        
    }

    public StartInstantRecordingOutput startInstantRecording(StartInstantRecordingInput inp)
    {
        StartInstantRecording startInstantRecording = new StartInstantRecording(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StartInstantRecordingOutput res = startInstantRecording.executeAction();
        return res;        
    }

    public DestoryGroupOwnerOutput destoryGroupOwner()
    {
        DestoryGroupOwner destoryGroupOwner = new DestoryGroupOwner(mainTVAgent2Service,  upnpService.getControlPoint());
        DestoryGroupOwnerOutput res = destoryGroupOwner.executeAction();
        return res;        
    }

    public SendMBRIRKeyOutput sendMBRIRKey(SendMBRIRKeyInput inp)
    {
        SendMBRIRKey sendMBRIRKey = new SendMBRIRKey(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SendMBRIRKeyOutput res = sendMBRIRKey.executeAction();
        return res;        
    }

    public SetHTSAllSpeakerLevelOutput setHTSAllSpeakerLevel(SetHTSAllSpeakerLevelInput inp)
    {
        SetHTSAllSpeakerLevel setHTSAllSpeakerLevel = new SetHTSAllSpeakerLevel(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetHTSAllSpeakerLevelOutput res = setHTSAllSpeakerLevel.executeAction();
        return res;        
    }

    public SetMainTVChannelOutput setMainTVChannel(SetMainTVChannelInput inp)
    {
        SetMainTVChannel setMainTVChannel = new SetMainTVChannel(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetMainTVChannelOutput res = setMainTVChannel.executeAction();
        return res;        
    }

    public GetBannerInformationOutput getBannerInformation()
    {
        GetBannerInformation getBannerInformation = new GetBannerInformation(mainTVAgent2Service,  upnpService.getControlPoint());
        GetBannerInformationOutput res = getBannerInformation.executeAction();
        return res;        
    }

    public ChangeScheduleOutput changeSchedule(ChangeScheduleInput inp)
    {
        ChangeSchedule changeSchedule = new ChangeSchedule(mainTVAgent2Service, inp, upnpService.getControlPoint());
        ChangeScheduleOutput res = changeSchedule.executeAction();
        return res;        
    }

    public EnforceAKEOutput enforceAKE()
    {
        EnforceAKE enforceAKE = new EnforceAKE(mainTVAgent2Service,  upnpService.getControlPoint());
        EnforceAKEOutput res = enforceAKE.executeAction();
        return res;        
    }

    public GetSourceListOutput getSourceList()
    {
        GetSourceList getSourceList = new GetSourceList(mainTVAgent2Service,  upnpService.getControlPoint());
        GetSourceListOutput res = getSourceList.executeAction();
        return res;        
    }

    public StartExtSourceViewOutput startExtSourceView(StartExtSourceViewInput inp)
    {
        StartExtSourceView startExtSourceView = new StartExtSourceView(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StartExtSourceViewOutput res = startExtSourceView.executeAction();
        return res;        
    }

    public DeleteRecordedItemOutput deleteRecordedItem(DeleteRecordedItemInput inp)
    {
        DeleteRecordedItem deleteRecordedItem = new DeleteRecordedItem(mainTVAgent2Service, inp, upnpService.getControlPoint());
        DeleteRecordedItemOutput res = deleteRecordedItem.executeAction();
        return res;        
    }

    public GetScheduleListURLOutput getScheduleListURL()
    {
        GetScheduleListURL getScheduleListURL = new GetScheduleListURL(mainTVAgent2Service,  upnpService.getControlPoint());
        GetScheduleListURLOutput res = getScheduleListURL.executeAction();
        return res;        
    }

    public GetHTSSpeakerConfigOutput getHTSSpeakerConfig()
    {
        GetHTSSpeakerConfig getHTSSpeakerConfig = new GetHTSSpeakerConfig(mainTVAgent2Service,  upnpService.getControlPoint());
        GetHTSSpeakerConfigOutput res = getHTSSpeakerConfig.executeAction();
        return res;        
    }

    public GetChannelListURLOutput getChannelListURL()
    {
        GetChannelListURL getChannelListURL = new GetChannelListURL(mainTVAgent2Service,  upnpService.getControlPoint());
        GetChannelListURLOutput res = getChannelListURL.executeAction();
        return res;        
    }

    public StopRecordOutput stopRecord(StopRecordInput inp)
    {
        StopRecord stopRecord = new StopRecord(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StopRecordOutput res = stopRecord.executeAction();
        return res;        
    }

    public DeleteScheduleOutput deleteSchedule(DeleteScheduleInput inp)
    {
        DeleteSchedule deleteSchedule = new DeleteSchedule(mainTVAgent2Service, inp, upnpService.getControlPoint());
        DeleteScheduleOutput res = deleteSchedule.executeAction();
        return res;        
    }

    public GetACRMessageOutput getACRMessage()
    {
        GetACRMessage getACRMessage = new GetACRMessage(mainTVAgent2Service,  upnpService.getControlPoint());
        GetACRMessageOutput res = getACRMessage.executeAction();
        return res;        
    }

    public SetHTSAllSpeakerDistanceOutput setHTSAllSpeakerDistance(SetHTSAllSpeakerDistanceInput inp)
    {
        SetHTSAllSpeakerDistance setHTSAllSpeakerDistance = new SetHTSAllSpeakerDistance(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetHTSAllSpeakerDistanceOutput res = setHTSAllSpeakerDistance.executeAction();
        return res;        
    }

    public GetRecordChannelOutput getRecordChannel()
    {
        GetRecordChannel getRecordChannel = new GetRecordChannel(mainTVAgent2Service,  upnpService.getControlPoint());
        GetRecordChannelOutput res = getRecordChannel.executeAction();
        return res;        
    }

    public PlayRecordedItemOutput playRecordedItem(PlayRecordedItemInput inp)
    {
        PlayRecordedItem playRecordedItem = new PlayRecordedItem(mainTVAgent2Service, inp, upnpService.getControlPoint());
        PlayRecordedItemOutput res = playRecordedItem.executeAction();
        return res;        
    }

    public SetRecordDurationOutput setRecordDuration(SetRecordDurationInput inp)
    {
        SetRecordDuration setRecordDuration = new SetRecordDuration(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetRecordDurationOutput res = setRecordDuration.executeAction();
        return res;        
    }

    public GetDTVInformationOutput getDTVInformation()
    {
        GetDTVInformation getDTVInformation = new GetDTVInformation(mainTVAgent2Service,  upnpService.getControlPoint());
        GetDTVInformationOutput res = getDTVInformation.executeAction();
        return res;        
    }

    public GetACRCurrentProgramNameOutput getACRCurrentProgramName()
    {
        GetACRCurrentProgramName getACRCurrentProgramName = new GetACRCurrentProgramName(mainTVAgent2Service,  upnpService.getControlPoint());
        GetACRCurrentProgramNameOutput res = getACRCurrentProgramName.executeAction();
        return res;        
    }

    public StartCloneViewOutput startCloneView(StartCloneViewInput inp)
    {
        StartCloneView startCloneView = new StartCloneView(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StartCloneViewOutput res = startCloneView.executeAction();
        return res;        
    }

    public StartSecondTVViewOutput startSecondTVView(StartSecondTVViewInput inp)
    {
        StartSecondTVView startSecondTVView = new StartSecondTVView(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StartSecondTVViewOutput res = startSecondTVView.executeAction();
        return res;        
    }

    public GetHTSSoundEffectOutput getHTSSoundEffect()
    {
        GetHTSSoundEffect getHTSSoundEffect = new GetHTSSoundEffect(mainTVAgent2Service,  upnpService.getControlPoint());
        GetHTSSoundEffectOutput res = getHTSSoundEffect.executeAction();
        return res;        
    }

    public GetAllProgramInformationURLOutput getAllProgramInformationURL(GetAllProgramInformationURLInput inp)
    {
        GetAllProgramInformationURL getAllProgramInformationURL = new GetAllProgramInformationURL(mainTVAgent2Service, inp, upnpService.getControlPoint());
        GetAllProgramInformationURLOutput res = getAllProgramInformationURL.executeAction();
        return res;        
    }

    public GetAvailableActionsOutput getAvailableActions()
    {
        GetAvailableActions getAvailableActions = new GetAvailableActions(mainTVAgent2Service,  upnpService.getControlPoint());
        GetAvailableActionsOutput res = getAvailableActions.executeAction();
        return res;        
    }

    public GetACRCurrentChannelNameOutput getACRCurrentChannelName()
    {
        GetACRCurrentChannelName getACRCurrentChannelName = new GetACRCurrentChannelName(mainTVAgent2Service,  upnpService.getControlPoint());
        GetACRCurrentChannelNameOutput res = getACRCurrentChannelName.executeAction();
        return res;        
    }

    public GetCurrentBrowserURLOutput getCurrentBrowserURL()
    {
        GetCurrentBrowserURL getCurrentBrowserURL = new GetCurrentBrowserURL(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentBrowserURLOutput res = getCurrentBrowserURL.executeAction();
        return res;        
    }

    public GetFilteredProgarmURLOutput getFilteredProgarmURL(GetFilteredProgarmURLInput inp)
    {
        GetFilteredProgarmURL getFilteredProgarmURL = new GetFilteredProgarmURL(mainTVAgent2Service, inp, upnpService.getControlPoint());
        GetFilteredProgarmURLOutput res = getFilteredProgarmURL.executeAction();
        return res;        
    }

    public StopBrowserOutput stopBrowser()
    {
        StopBrowser stopBrowser = new StopBrowser(mainTVAgent2Service,  upnpService.getControlPoint());
        StopBrowserOutput res = stopBrowser.executeAction();
        return res;        
    }

    public GetCurrentExternalSourceOutput getCurrentExternalSource()
    {
        GetCurrentExternalSource getCurrentExternalSource = new GetCurrentExternalSource(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentExternalSourceOutput res = getCurrentExternalSource.executeAction();
        return res;        
    }

    public StopViewOutput stopView(StopViewInput inp)
    {
        StopView stopView = new StopView(mainTVAgent2Service, inp, upnpService.getControlPoint());
        StopViewOutput res = stopView.executeAction();
        return res;        
    }

    public SetHTSSoundEffectOutput setHTSSoundEffect(SetHTSSoundEffectInput inp)
    {
        SetHTSSoundEffect setHTSSoundEffect = new SetHTSSoundEffect(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SetHTSSoundEffectOutput res = setHTSSoundEffect.executeAction();
        return res;        
    }

    public GetDetailProgramInformationOutput getDetailProgramInformation(GetDetailProgramInformationInput inp)
    {
        GetDetailProgramInformation getDetailProgramInformation = new GetDetailProgramInformation(mainTVAgent2Service, inp, upnpService.getControlPoint());
        GetDetailProgramInformationOutput res = getDetailProgramInformation.executeAction();
        return res;        
    }

    public GetMBRDongleStatusOutput getMBRDongleStatus()
    {
        GetMBRDongleStatus getMBRDongleStatus = new GetMBRDongleStatus(mainTVAgent2Service,  upnpService.getControlPoint());
        GetMBRDongleStatusOutput res = getMBRDongleStatus.executeAction();
        return res;        
    }

    public SendBrowserCommandOutput sendBrowserCommand(SendBrowserCommandInput inp)
    {
        SendBrowserCommand sendBrowserCommand = new SendBrowserCommand(mainTVAgent2Service, inp, upnpService.getControlPoint());
        SendBrowserCommandOutput res = sendBrowserCommand.executeAction();
        return res;        
    }

    public RunBrowserOutput runBrowser(RunBrowserInput inp)
    {
        RunBrowser runBrowser = new RunBrowser(mainTVAgent2Service, inp, upnpService.getControlPoint());
        RunBrowserOutput res = runBrowser.executeAction();
        return res;        
    }

    public GetCurrentBrowserModeOutput getCurrentBrowserMode()
    {
        GetCurrentBrowserMode getCurrentBrowserMode = new GetCurrentBrowserMode(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentBrowserModeOutput res = getCurrentBrowserMode.executeAction();
        return res;        
    }

    public GetCurrentHTSSpeakerLayoutOutput getCurrentHTSSpeakerLayout()
    {
        GetCurrentHTSSpeakerLayout getCurrentHTSSpeakerLayout = new GetCurrentHTSSpeakerLayout(mainTVAgent2Service,  upnpService.getControlPoint());
        GetCurrentHTSSpeakerLayoutOutput res = getCurrentHTSSpeakerLayout.executeAction();
        return res;        
    }
}
