package nextcp.upnp.modelGen.schemastencentcom.qPlay2;

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

import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetTracksCount;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetTracksCountOutput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.RemoveTracks;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.RemoveTracksOutput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.RemoveTracksInput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetLyricSupportType;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetLyricSupportTypeOutput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetMaxTracks;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetMaxTracksOutput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetTracksInfo;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetTracksInfoOutput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.GetTracksInfoInput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.SetTracksInfo;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.SetTracksInfoOutput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.SetTracksInfoInput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.QPlayAuth;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.QPlayAuthOutput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.QPlayAuthInput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.SetNetwork;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.SetNetworkInput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.SetLyric;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.SetLyricInput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.InsertTracks;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.InsertTracksOutput;
import nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions.InsertTracksInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class QPlayService
{
    private static Logger log = LoggerFactory.getLogger(QPlayService.class.getName());

    private RemoteService qPlayService = null;

    private UpnpService upnpService = null;

    private QPlayServiceSubscription subscription = null;
    
    public QPlayService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        qPlayService = device.findService(new ServiceType("schemas-tencent-com", "QPlay"));
        if (qPlayService != null)
        {
	        subscription = new QPlayServiceSubscription(qPlayService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'QPlay' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'QPlay' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IQPlayServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IQPlayServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getQPlayService()
    {
        return qPlayService;
    }    


//
// Actions
// =========================================================================
//



    public GetTracksCountOutput getTracksCount()
    {
        GetTracksCount getTracksCount = new GetTracksCount(qPlayService,  upnpService.getControlPoint());
        GetTracksCountOutput res = getTracksCount.executeAction();
        return res;        
    }

    public RemoveTracksOutput removeTracks(RemoveTracksInput inp)
    {
        RemoveTracks removeTracks = new RemoveTracks(qPlayService, inp, upnpService.getControlPoint());
        RemoveTracksOutput res = removeTracks.executeAction();
        return res;        
    }

    public GetLyricSupportTypeOutput getLyricSupportType()
    {
        GetLyricSupportType getLyricSupportType = new GetLyricSupportType(qPlayService,  upnpService.getControlPoint());
        GetLyricSupportTypeOutput res = getLyricSupportType.executeAction();
        return res;        
    }

    public GetMaxTracksOutput getMaxTracks()
    {
        GetMaxTracks getMaxTracks = new GetMaxTracks(qPlayService,  upnpService.getControlPoint());
        GetMaxTracksOutput res = getMaxTracks.executeAction();
        return res;        
    }

    public GetTracksInfoOutput getTracksInfo(GetTracksInfoInput inp)
    {
        GetTracksInfo getTracksInfo = new GetTracksInfo(qPlayService, inp, upnpService.getControlPoint());
        GetTracksInfoOutput res = getTracksInfo.executeAction();
        return res;        
    }

    public SetTracksInfoOutput setTracksInfo(SetTracksInfoInput inp)
    {
        SetTracksInfo setTracksInfo = new SetTracksInfo(qPlayService, inp, upnpService.getControlPoint());
        SetTracksInfoOutput res = setTracksInfo.executeAction();
        return res;        
    }

    public QPlayAuthOutput qPlayAuth(QPlayAuthInput inp)
    {
        QPlayAuth qPlayAuth = new QPlayAuth(qPlayService, inp, upnpService.getControlPoint());
        QPlayAuthOutput res = qPlayAuth.executeAction();
        return res;        
    }

    public void setNetwork(SetNetworkInput inp)
    {
        SetNetwork setNetwork = new SetNetwork(qPlayService, inp, upnpService.getControlPoint());
        setNetwork.executeAction();
    }

    public void setLyric(SetLyricInput inp)
    {
        SetLyric setLyric = new SetLyric(qPlayService, inp, upnpService.getControlPoint());
        setLyric.executeAction();
    }

    public InsertTracksOutput insertTracks(InsertTracksInput inp)
    {
        InsertTracks insertTracks = new InsertTracks(qPlayService, inp, upnpService.getControlPoint());
        InsertTracksOutput res = insertTracks.executeAction();
        return res;        
    }
}
