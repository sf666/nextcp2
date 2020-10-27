package nextcp.upnp.modelGen.jminimorg.monitor;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.jminimorg.monitor.actions.InstallPackage;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.InstallPackageInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetModuleStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetModuleStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetModuleStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.RemovePackage;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.RemovePackageInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetRuntimeVersion;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetRuntimeVersionOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllComponentStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllComponentStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetLogData;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetLogDataOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetLogDataInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.SaveProperty;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.SavePropertyOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.SavePropertyInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackageProperty;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackagePropertyOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackagePropertyInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.StopRuntime;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPropertyUpdates;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPropertyUpdatesOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetComponentStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetComponentStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetComponentStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllContextStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllContextStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetErrorInfo;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetErrorInfoOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetComponentContextStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetComponentContextStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetComponentContextStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.UndoPackage;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.UndoPackageInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetContextStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetContextStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetContextStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAboutInfo;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAboutInfoOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllModuleStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllModuleStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.StopContext;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.StopContextInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackageAboutInfo;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackageAboutInfoOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackageAboutInfoInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllPackageStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllPackageStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.CheckPackage;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.CheckPackageInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetHostAndPort;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetHostAndPortOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.StartContext;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.StartContextInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllPackageProperties;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllPackagePropertiesOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetAllPackagePropertiesInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetProperty;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPropertyOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPropertyInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackageStatus;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackageStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetPackageStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.RelaunchRuntime;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetContextComponentStatusList;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetContextComponentStatusListOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.GetContextComponentStatusListInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.SetProperty;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.SetPropertyOutput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.SetPropertyInput;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.ReplacePackage;
import nextcp.upnp.modelGen.jminimorg.monitor.actions.ReplacePackageInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class MonitorService
{
    private static Logger log = LoggerFactory.getLogger(MonitorService.class.getName());

    private RemoteService monitorService = null;

    private UpnpService upnpService = null;

    private MonitorServiceStateVariable monitorServiceStateVariable = new MonitorServiceStateVariable();
    
    private MonitorServiceSubscription subscription = null;
    
    public MonitorService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        monitorService = device.findService(new ServiceType("jminim-org", "Monitor"));
        subscription = new MonitorServiceSubscription(monitorService, 600);
        try
        {
            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
            protocol.run();
        }
        catch (ProtocolCreationException ex)
        {
            log.error("Event subscription", ex);
        }

        log.info(String.format("initialized service 'Monitor' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
    }
    
    public void addSubscriptionEventListener(IMonitorServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IMonitorServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getMonitorService()
    {
        return monitorService;
    }    


    public void installPackage(InstallPackageInput inp)
    {
        InstallPackage installPackage = new InstallPackage(monitorService, inp, upnpService.getControlPoint());
        installPackage.executeAction();
    }

    public GetModuleStatusOutput getModuleStatus(GetModuleStatusInput inp)
    {
        GetModuleStatus getModuleStatus = new GetModuleStatus(monitorService, inp, upnpService.getControlPoint());
        GetModuleStatusOutput res = getModuleStatus.executeAction();
        return res;        
    }

    public void removePackage(RemovePackageInput inp)
    {
        RemovePackage removePackage = new RemovePackage(monitorService, inp, upnpService.getControlPoint());
        removePackage.executeAction();
    }

    public GetRuntimeVersionOutput getRuntimeVersion()
    {
        GetRuntimeVersion getRuntimeVersion = new GetRuntimeVersion(monitorService,  upnpService.getControlPoint());
        GetRuntimeVersionOutput res = getRuntimeVersion.executeAction();
        return res;        
    }

    public GetAllComponentStatusOutput getAllComponentStatus()
    {
        GetAllComponentStatus getAllComponentStatus = new GetAllComponentStatus(monitorService,  upnpService.getControlPoint());
        GetAllComponentStatusOutput res = getAllComponentStatus.executeAction();
        return res;        
    }

    public GetLogDataOutput getLogData(GetLogDataInput inp)
    {
        GetLogData getLogData = new GetLogData(monitorService, inp, upnpService.getControlPoint());
        GetLogDataOutput res = getLogData.executeAction();
        return res;        
    }

    public SavePropertyOutput saveProperty(SavePropertyInput inp)
    {
        SaveProperty saveProperty = new SaveProperty(monitorService, inp, upnpService.getControlPoint());
        SavePropertyOutput res = saveProperty.executeAction();
        return res;        
    }

    public GetPackagePropertyOutput getPackageProperty(GetPackagePropertyInput inp)
    {
        GetPackageProperty getPackageProperty = new GetPackageProperty(monitorService, inp, upnpService.getControlPoint());
        GetPackagePropertyOutput res = getPackageProperty.executeAction();
        return res;        
    }

    public void stopRuntime()
    {
        StopRuntime stopRuntime = new StopRuntime(monitorService,  upnpService.getControlPoint());
        stopRuntime.executeAction();
    }

    public GetPropertyUpdatesOutput getPropertyUpdates()
    {
        GetPropertyUpdates getPropertyUpdates = new GetPropertyUpdates(monitorService,  upnpService.getControlPoint());
        GetPropertyUpdatesOutput res = getPropertyUpdates.executeAction();
        return res;        
    }

    public GetComponentStatusOutput getComponentStatus(GetComponentStatusInput inp)
    {
        GetComponentStatus getComponentStatus = new GetComponentStatus(monitorService, inp, upnpService.getControlPoint());
        GetComponentStatusOutput res = getComponentStatus.executeAction();
        return res;        
    }

    public GetAllContextStatusOutput getAllContextStatus()
    {
        GetAllContextStatus getAllContextStatus = new GetAllContextStatus(monitorService,  upnpService.getControlPoint());
        GetAllContextStatusOutput res = getAllContextStatus.executeAction();
        return res;        
    }

    public GetErrorInfoOutput getErrorInfo()
    {
        GetErrorInfo getErrorInfo = new GetErrorInfo(monitorService,  upnpService.getControlPoint());
        GetErrorInfoOutput res = getErrorInfo.executeAction();
        return res;        
    }

    public GetComponentContextStatusOutput getComponentContextStatus(GetComponentContextStatusInput inp)
    {
        GetComponentContextStatus getComponentContextStatus = new GetComponentContextStatus(monitorService, inp, upnpService.getControlPoint());
        GetComponentContextStatusOutput res = getComponentContextStatus.executeAction();
        return res;        
    }

    public void undoPackage(UndoPackageInput inp)
    {
        UndoPackage undoPackage = new UndoPackage(monitorService, inp, upnpService.getControlPoint());
        undoPackage.executeAction();
    }

    public GetContextStatusOutput getContextStatus(GetContextStatusInput inp)
    {
        GetContextStatus getContextStatus = new GetContextStatus(monitorService, inp, upnpService.getControlPoint());
        GetContextStatusOutput res = getContextStatus.executeAction();
        return res;        
    }

    public GetAboutInfoOutput getAboutInfo()
    {
        GetAboutInfo getAboutInfo = new GetAboutInfo(monitorService,  upnpService.getControlPoint());
        GetAboutInfoOutput res = getAboutInfo.executeAction();
        return res;        
    }

    public GetAllModuleStatusOutput getAllModuleStatus()
    {
        GetAllModuleStatus getAllModuleStatus = new GetAllModuleStatus(monitorService,  upnpService.getControlPoint());
        GetAllModuleStatusOutput res = getAllModuleStatus.executeAction();
        return res;        
    }

    public void stopContext(StopContextInput inp)
    {
        StopContext stopContext = new StopContext(monitorService, inp, upnpService.getControlPoint());
        stopContext.executeAction();
    }

    public GetPackageAboutInfoOutput getPackageAboutInfo(GetPackageAboutInfoInput inp)
    {
        GetPackageAboutInfo getPackageAboutInfo = new GetPackageAboutInfo(monitorService, inp, upnpService.getControlPoint());
        GetPackageAboutInfoOutput res = getPackageAboutInfo.executeAction();
        return res;        
    }

    public GetAllPackageStatusOutput getAllPackageStatus()
    {
        GetAllPackageStatus getAllPackageStatus = new GetAllPackageStatus(monitorService,  upnpService.getControlPoint());
        GetAllPackageStatusOutput res = getAllPackageStatus.executeAction();
        return res;        
    }

    public void checkPackage(CheckPackageInput inp)
    {
        CheckPackage checkPackage = new CheckPackage(monitorService, inp, upnpService.getControlPoint());
        checkPackage.executeAction();
    }

    public GetHostAndPortOutput getHostAndPort()
    {
        GetHostAndPort getHostAndPort = new GetHostAndPort(monitorService,  upnpService.getControlPoint());
        GetHostAndPortOutput res = getHostAndPort.executeAction();
        return res;        
    }

    public void startContext(StartContextInput inp)
    {
        StartContext startContext = new StartContext(monitorService, inp, upnpService.getControlPoint());
        startContext.executeAction();
    }

    public GetAllPackagePropertiesOutput getAllPackageProperties(GetAllPackagePropertiesInput inp)
    {
        GetAllPackageProperties getAllPackageProperties = new GetAllPackageProperties(monitorService, inp, upnpService.getControlPoint());
        GetAllPackagePropertiesOutput res = getAllPackageProperties.executeAction();
        return res;        
    }

    public GetPropertyOutput getProperty(GetPropertyInput inp)
    {
        GetProperty getProperty = new GetProperty(monitorService, inp, upnpService.getControlPoint());
        GetPropertyOutput res = getProperty.executeAction();
        return res;        
    }

    public GetPackageStatusOutput getPackageStatus(GetPackageStatusInput inp)
    {
        GetPackageStatus getPackageStatus = new GetPackageStatus(monitorService, inp, upnpService.getControlPoint());
        GetPackageStatusOutput res = getPackageStatus.executeAction();
        return res;        
    }

    public void relaunchRuntime()
    {
        RelaunchRuntime relaunchRuntime = new RelaunchRuntime(monitorService,  upnpService.getControlPoint());
        relaunchRuntime.executeAction();
    }

    public GetContextComponentStatusListOutput getContextComponentStatusList(GetContextComponentStatusListInput inp)
    {
        GetContextComponentStatusList getContextComponentStatusList = new GetContextComponentStatusList(monitorService, inp, upnpService.getControlPoint());
        GetContextComponentStatusListOutput res = getContextComponentStatusList.executeAction();
        return res;        
    }

    public SetPropertyOutput setProperty(SetPropertyInput inp)
    {
        SetProperty setProperty = new SetProperty(monitorService, inp, upnpService.getControlPoint());
        SetPropertyOutput res = setProperty.executeAction();
        return res;        
    }

    public void replacePackage(ReplacePackageInput inp)
    {
        ReplacePackage replacePackage = new ReplacePackage(monitorService, inp, upnpService.getControlPoint());
        replacePackage.executeAction();
    }
}
