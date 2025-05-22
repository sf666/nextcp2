package nextcp.upnp.modelGen.avopenhomeorg.pins1;

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

import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.SetDevice;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.SetDeviceInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.GetIdArray;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.GetIdArrayOutput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.Swap;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.SwapInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.ReadList;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.ReadListOutput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.ReadListInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.InvokeIndex;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.InvokeIndexInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.GetModes;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.GetModesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.SetAccount;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.SetAccountInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.InvokeId;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.InvokeIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.GetDeviceAccountMax;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.GetDeviceAccountMaxOutput;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.Clear;
import nextcp.upnp.modelGen.avopenhomeorg.pins1.actions.ClearInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class PinsService
{
    private static Logger log = LoggerFactory.getLogger(PinsService.class.getName());

    private RemoteService pinsService = null;

    private UpnpService upnpService = null;

//    private PinsServiceStateVariable pinsServiceStateVariable = new PinsServiceStateVariable();
    
    private PinsServiceSubscription subscription = null;
    
    public PinsService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        pinsService = device.findService(new ServiceType("av-openhome-org", "Pins"));
        if (pinsService != null)
        {
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
	    else
	    {
	        log.warn(String.format("initialized service 'Pins' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IPinsServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IPinsServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getPinsService()
    {
        return pinsService;
    }    


//
// Actions
// =========================================================================
//



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
