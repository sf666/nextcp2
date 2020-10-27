package nextcp.upnp.modelGen.avopenhomeorg.pins;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.SetDevice;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.SetDeviceInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.GetIdArray;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.GetIdArrayOutput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.Swap;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.SwapInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.ReadList;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.ReadListOutput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.ReadListInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.InvokeIndex;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.InvokeIndexInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.GetModes;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.GetModesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.SetAccount;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.SetAccountInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.InvokeId;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.InvokeIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.GetDeviceAccountMax;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.GetDeviceAccountMaxOutput;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.Clear;
import nextcp.upnp.modelGen.avopenhomeorg.pins.actions.ClearInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class PinsService
{
    private static Logger log = LoggerFactory.getLogger(PinsService.class.getName());

    private RemoteService pinsService = null;

    private UpnpService upnpService = null;

    private PinsServiceStateVariable pinsServiceStateVariable = new PinsServiceStateVariable();
    
    private PinsServiceSubscription subscription = null;
    
    public PinsService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        pinsService = device.findService(new ServiceType("av-openhome-org", "Pins"));
        subscription = new PinsServiceSubscription(pinsService, 600);
        try
        {
            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
            protocol.run();
        }
        catch (ProtocolCreationException ex)
        {
            log.error("Event subscription", ex);
        }

        log.info(String.format("initialized service 'Pins' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
    }
    
    public void addSubscriptionEventListener(IPinsServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IPinsServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getPinsService()
    {
        return pinsService;
    }    


    public void setDevice(SetDeviceInput inp)
    {
        SetDevice setDevice = new SetDevice(pinsService, inp, upnpService.getControlPoint());
        setDevice.executeAction();
    }

    public GetIdArrayOutput getIdArray()
    {
        GetIdArray getIdArray = new GetIdArray(pinsService,  upnpService.getControlPoint());
        GetIdArrayOutput res = getIdArray.executeAction();
        return res;        
    }

    public void swap(SwapInput inp)
    {
        Swap swap = new Swap(pinsService, inp, upnpService.getControlPoint());
        swap.executeAction();
    }

    public ReadListOutput readList(ReadListInput inp)
    {
        ReadList readList = new ReadList(pinsService, inp, upnpService.getControlPoint());
        ReadListOutput res = readList.executeAction();
        return res;        
    }

    public void invokeIndex(InvokeIndexInput inp)
    {
        InvokeIndex invokeIndex = new InvokeIndex(pinsService, inp, upnpService.getControlPoint());
        invokeIndex.executeAction();
    }

    public GetModesOutput getModes()
    {
        GetModes getModes = new GetModes(pinsService,  upnpService.getControlPoint());
        GetModesOutput res = getModes.executeAction();
        return res;        
    }

    public void setAccount(SetAccountInput inp)
    {
        SetAccount setAccount = new SetAccount(pinsService, inp, upnpService.getControlPoint());
        setAccount.executeAction();
    }

    public void invokeId(InvokeIdInput inp)
    {
        InvokeId invokeId = new InvokeId(pinsService, inp, upnpService.getControlPoint());
        invokeId.executeAction();
    }

    public GetDeviceAccountMaxOutput getDeviceAccountMax()
    {
        GetDeviceAccountMax getDeviceAccountMax = new GetDeviceAccountMax(pinsService,  upnpService.getControlPoint());
        GetDeviceAccountMaxOutput res = getDeviceAccountMax.executeAction();
        return res;        
    }

    public void clear(ClearInput inp)
    {
        Clear clear = new Clear(pinsService, inp, upnpService.getControlPoint());
        clear.executeAction();
    }
}
