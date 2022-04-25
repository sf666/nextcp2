package nextcp.upnp.modelGen.avopenhomeorg.radio;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.IdArrayChanged;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.IdArrayChangedOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.IdArrayChangedInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.Pause;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.Stop;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.Channel;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ChannelOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.IdArray;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.IdArrayOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.TransportState;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.TransportStateOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.SeekSecondAbsolute;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.SeekSecondAbsoluteInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.Read;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ReadOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ReadInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.Play;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ReadList;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ReadListOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ReadListInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.SeekSecondRelative;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.SeekSecondRelativeInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ProtocolInfo;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ProtocolInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.SetChannel;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.SetChannelInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ChannelsMax;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.ChannelsMaxOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.Id;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.IdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.SetId;
import nextcp.upnp.modelGen.avopenhomeorg.radio.actions.SetIdInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class RadioService
{
    private static Logger log = LoggerFactory.getLogger(RadioService.class.getName());

    private RemoteService radioService = null;

    private UpnpService upnpService = null;

    private RadioServiceStateVariable radioServiceStateVariable = new RadioServiceStateVariable();
    
    private RadioServiceSubscription subscription = null;
    
    public RadioService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        radioService = device.findService(new ServiceType("av-openhome-org", "Radio"));
        if (radioService != null)
        {
	        subscription = new RadioServiceSubscription(radioService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Radio' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Radio' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(IRadioServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IRadioServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getRadioService()
    {
        return radioService;
    }    


    public IdArrayChangedOutput idArrayChanged(IdArrayChangedInput inp)
    {
        IdArrayChanged idArrayChanged = new IdArrayChanged(radioService, inp, upnpService.getControlPoint());
        IdArrayChangedOutput res = idArrayChanged.executeAction();
        return res;        
    }

    public void pause()
    {
        Pause pause = new Pause(radioService,  upnpService.getControlPoint());
        pause.executeAction();
    }

    public void stop()
    {
        Stop stop = new Stop(radioService,  upnpService.getControlPoint());
        stop.executeAction();
    }

    public ChannelOutput channel()
    {
        Channel channel = new Channel(radioService,  upnpService.getControlPoint());
        ChannelOutput res = channel.executeAction();
        return res;        
    }

    public IdArrayOutput idArray()
    {
        IdArray idArray = new IdArray(radioService,  upnpService.getControlPoint());
        IdArrayOutput res = idArray.executeAction();
        return res;        
    }

    public TransportStateOutput transportState()
    {
        TransportState transportState = new TransportState(radioService,  upnpService.getControlPoint());
        TransportStateOutput res = transportState.executeAction();
        return res;        
    }

    public void seekSecondAbsolute(SeekSecondAbsoluteInput inp)
    {
        SeekSecondAbsolute seekSecondAbsolute = new SeekSecondAbsolute(radioService, inp, upnpService.getControlPoint());
        seekSecondAbsolute.executeAction();
    }

    public ReadOutput read(ReadInput inp)
    {
        Read read = new Read(radioService, inp, upnpService.getControlPoint());
        ReadOutput res = read.executeAction();
        return res;        
    }

    public void play()
    {
        Play play = new Play(radioService,  upnpService.getControlPoint());
        play.executeAction();
    }

    public ReadListOutput readList(ReadListInput inp)
    {
        ReadList readList = new ReadList(radioService, inp, upnpService.getControlPoint());
        ReadListOutput res = readList.executeAction();
        return res;        
    }

    public void seekSecondRelative(SeekSecondRelativeInput inp)
    {
        SeekSecondRelative seekSecondRelative = new SeekSecondRelative(radioService, inp, upnpService.getControlPoint());
        seekSecondRelative.executeAction();
    }

    public ProtocolInfoOutput protocolInfo()
    {
        ProtocolInfo protocolInfo = new ProtocolInfo(radioService,  upnpService.getControlPoint());
        ProtocolInfoOutput res = protocolInfo.executeAction();
        return res;        
    }

    public void setChannel(SetChannelInput inp)
    {
        SetChannel setChannel = new SetChannel(radioService, inp, upnpService.getControlPoint());
        setChannel.executeAction();
    }

    public ChannelsMaxOutput channelsMax()
    {
        ChannelsMax channelsMax = new ChannelsMax(radioService,  upnpService.getControlPoint());
        ChannelsMaxOutput res = channelsMax.executeAction();
        return res;        
    }

    public IdOutput id()
    {
        Id id = new Id(radioService,  upnpService.getControlPoint());
        IdOutput res = id.executeAction();
        return res;        
    }

    public void setId(SetIdInput inp)
    {
        SetId setId = new SetId(radioService, inp, upnpService.getControlPoint());
        setId.executeAction();
    }
}
