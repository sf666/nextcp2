package nextcp.upnp.modelGen.jminimorg.log2;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogDataLength;
import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogDataLengthOutput;
import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogStart;
import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogStartOutput;
import nextcp.upnp.modelGen.jminimorg.log2.actions.SetLogStart;
import nextcp.upnp.modelGen.jminimorg.log2.actions.SetLogStartInput;
import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogData;
import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogDataOutput;
import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogDataInput;
import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogLevel;
import nextcp.upnp.modelGen.jminimorg.log2.actions.GetLogLevelOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class Log2Service
{
    private static Logger log = LoggerFactory.getLogger(Log2Service.class.getName());

    private RemoteService log2Service = null;

    private UpnpService upnpService = null;

    private Log2ServiceStateVariable log2ServiceStateVariable = new Log2ServiceStateVariable();
    
    private Log2ServiceSubscription subscription = null;
    
    public Log2Service(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        log2Service = device.findService(new ServiceType("jminim-org", "Log2"));
        subscription = new Log2ServiceSubscription(log2Service, 600);
        try
        {
            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
            protocol.run();
        }
        catch (ProtocolCreationException ex)
        {
            log.error("Event subscription", ex);
        }

        log.info(String.format("initialized service 'Log2' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
    }
    
    public void addSubscriptionEventListener(ILog2ServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(ILog2ServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getLog2Service()
    {
        return log2Service;
    }    


    public GetLogDataLengthOutput getLogDataLength()
    {
        GetLogDataLength getLogDataLength = new GetLogDataLength(log2Service,  upnpService.getControlPoint());
        GetLogDataLengthOutput res = getLogDataLength.executeAction();
        return res;        
    }

    public GetLogStartOutput getLogStart()
    {
        GetLogStart getLogStart = new GetLogStart(log2Service,  upnpService.getControlPoint());
        GetLogStartOutput res = getLogStart.executeAction();
        return res;        
    }

    public void setLogStart(SetLogStartInput inp)
    {
        SetLogStart setLogStart = new SetLogStart(log2Service, inp, upnpService.getControlPoint());
        setLogStart.executeAction();
    }

    public GetLogDataOutput getLogData(GetLogDataInput inp)
    {
        GetLogData getLogData = new GetLogData(log2Service, inp, upnpService.getControlPoint());
        GetLogDataOutput res = getLogData.executeAction();
        return res;        
    }

    public GetLogLevelOutput getLogLevel()
    {
        GetLogLevel getLogLevel = new GetLogLevel(log2Service,  upnpService.getControlPoint());
        GetLogLevelOutput res = getLogLevel.executeAction();
        return res;        
    }
}
