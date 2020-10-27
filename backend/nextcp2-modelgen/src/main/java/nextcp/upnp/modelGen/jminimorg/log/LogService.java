package nextcp.upnp.modelGen.jminimorg.log;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogDataLength;
import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogDataLengthOutput;
import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogStart;
import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogStartOutput;
import nextcp.upnp.modelGen.jminimorg.log.actions.SetLogStart;
import nextcp.upnp.modelGen.jminimorg.log.actions.SetLogStartInput;
import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogData;
import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogDataOutput;
import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogDataInput;
import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogLevel;
import nextcp.upnp.modelGen.jminimorg.log.actions.GetLogLevelOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class LogService
{
    private static Logger log = LoggerFactory.getLogger(LogService.class.getName());

    private RemoteService logService = null;

    private UpnpService upnpService = null;

    private LogServiceStateVariable logServiceStateVariable = new LogServiceStateVariable();
    
    private LogServiceSubscription subscription = null;
    
    public LogService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        logService = device.findService(new ServiceType("jminim-org", "Log"));
        subscription = new LogServiceSubscription(logService, 600);
        try
        {
            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
            protocol.run();
        }
        catch (ProtocolCreationException ex)
        {
            log.error("Event subscription", ex);
        }

        log.info(String.format("initialized service 'Log' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
    }
    
    public void addSubscriptionEventListener(ILogServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(ILogServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getLogService()
    {
        return logService;
    }    


    public GetLogDataLengthOutput getLogDataLength()
    {
        GetLogDataLength getLogDataLength = new GetLogDataLength(logService,  upnpService.getControlPoint());
        GetLogDataLengthOutput res = getLogDataLength.executeAction();
        return res;        
    }

    public GetLogStartOutput getLogStart()
    {
        GetLogStart getLogStart = new GetLogStart(logService,  upnpService.getControlPoint());
        GetLogStartOutput res = getLogStart.executeAction();
        return res;        
    }

    public void setLogStart(SetLogStartInput inp)
    {
        SetLogStart setLogStart = new SetLogStart(logService, inp, upnpService.getControlPoint());
        setLogStart.executeAction();
    }

    public GetLogDataOutput getLogData(GetLogDataInput inp)
    {
        GetLogData getLogData = new GetLogData(logService, inp, upnpService.getControlPoint());
        GetLogDataOutput res = getLogData.executeAction();
        return res;        
    }

    public GetLogLevelOutput getLogLevel()
    {
        GetLogLevel getLogLevel = new GetLogLevel(logService,  upnpService.getControlPoint());
        GetLogLevelOutput res = getLogLevel.executeAction();
        return res;        
    }
}
