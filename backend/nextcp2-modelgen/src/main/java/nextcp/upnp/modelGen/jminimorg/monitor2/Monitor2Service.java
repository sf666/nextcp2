package nextcp.upnp.modelGen.jminimorg.monitor2;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.jminimorg.monitor2.actions.InstallPackage;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.InstallPackageInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetModuleStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetModuleStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetModuleStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.RemovePackage;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.RemovePackageInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetRuntimeVersion;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetRuntimeVersionOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllComponentStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllComponentStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetLogData;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetLogDataOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetLogDataInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.SaveProperty;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.SavePropertyOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.SavePropertyInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackageProperty;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackagePropertyOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackagePropertyInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.StopRuntime;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPropertyUpdates;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPropertyUpdatesOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetComponentStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetComponentStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetComponentStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllContextStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllContextStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetErrorInfo;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetErrorInfoOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetComponentContextStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetComponentContextStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetComponentContextStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.UndoPackage;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.UndoPackageInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetContextStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetContextStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetContextStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAboutInfo;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAboutInfoOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllModuleStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllModuleStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.StopContext;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.StopContextInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackageAboutInfo;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackageAboutInfoOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackageAboutInfoInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllPackageStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllPackageStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.CheckPackageLicenseType;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.CheckPackageLicenseTypeInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.CheckPackage;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.CheckPackageInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetHostAndPort;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetHostAndPortOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.StartContext;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.StartContextInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllPackageProperties;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllPackagePropertiesOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetAllPackagePropertiesInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetProperty;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPropertyOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPropertyInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackageStatus;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackageStatusOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetPackageStatusInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.RelaunchRuntime;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetContextComponentStatusList;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetContextComponentStatusListOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.GetContextComponentStatusListInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.SetProperty;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.SetPropertyOutput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.SetPropertyInput;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.ReplacePackage;
import nextcp.upnp.modelGen.jminimorg.monitor2.actions.ReplacePackageInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class Monitor2Service
{
    private static Logger log = LoggerFactory.getLogger(Monitor2Service.class.getName());

    private RemoteService monitor2Service = null;

    private UpnpService upnpService = null;

    private Monitor2ServiceStateVariable monitor2ServiceStateVariable = new Monitor2ServiceStateVariable();
    
    private Monitor2ServiceSubscription subscription = null;
    
    public Monitor2Service(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        monitor2Service = device.findService(new ServiceType("jminim-org", "Monitor2"));
        subscription = new Monitor2ServiceSubscription(monitor2Service, 600);
        try
        {
            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
            protocol.run();
        }
        catch (ProtocolCreationException ex)
        {
            log.error("Event subscription", ex);
        }

        log.info(String.format("initialized service 'Monitor2' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
    }
    
    public void addSubscriptionEventListener(IMonitor2ServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IMonitor2ServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getMonitor2Service()
    {
        return monitor2Service;
    }    


    public void installPackage(InstallPackageInput inp)
    {
        InstallPackage installPackage = new InstallPackage(monitor2Service, inp, upnpService.getControlPoint());
        installPackage.executeAction();
    }

    public GetModuleStatusOutput getModuleStatus(GetModuleStatusInput inp)
    {
        GetModuleStatus getModuleStatus = new GetModuleStatus(monitor2Service, inp, upnpService.getControlPoint());
        GetModuleStatusOutput res = getModuleStatus.executeAction();
        return res;        
    }

    public void removePackage(RemovePackageInput inp)
    {
        RemovePackage removePackage = new RemovePackage(monitor2Service, inp, upnpService.getControlPoint());
        removePackage.executeAction();
    }

    public GetRuntimeVersionOutput getRuntimeVersion()
    {
        GetRuntimeVersion getRuntimeVersion = new GetRuntimeVersion(monitor2Service,  upnpService.getControlPoint());
        GetRuntimeVersionOutput res = getRuntimeVersion.executeAction();
        return res;        
    }

    public GetAllComponentStatusOutput getAllComponentStatus()
    {
        GetAllComponentStatus getAllComponentStatus = new GetAllComponentStatus(monitor2Service,  upnpService.getControlPoint());
        GetAllComponentStatusOutput res = getAllComponentStatus.executeAction();
        return res;        
    }

    public GetLogDataOutput getLogData(GetLogDataInput inp)
    {
        GetLogData getLogData = new GetLogData(monitor2Service, inp, upnpService.getControlPoint());
        GetLogDataOutput res = getLogData.executeAction();
        return res;        
    }

    public SavePropertyOutput saveProperty(SavePropertyInput inp)
    {
        SaveProperty saveProperty = new SaveProperty(monitor2Service, inp, upnpService.getControlPoint());
        SavePropertyOutput res = saveProperty.executeAction();
        return res;        
    }

    public GetPackagePropertyOutput getPackageProperty(GetPackagePropertyInput inp)
    {
        GetPackageProperty getPackageProperty = new GetPackageProperty(monitor2Service, inp, upnpService.getControlPoint());
        GetPackagePropertyOutput res = getPackageProperty.executeAction();
        return res;        
    }

    public void stopRuntime()
    {
        StopRuntime stopRuntime = new StopRuntime(monitor2Service,  upnpService.getControlPoint());
        stopRuntime.executeAction();
    }

    public GetPropertyUpdatesOutput getPropertyUpdates()
    {
        GetPropertyUpdates getPropertyUpdates = new GetPropertyUpdates(monitor2Service,  upnpService.getControlPoint());
        GetPropertyUpdatesOutput res = getPropertyUpdates.executeAction();
        return res;        
    }

    public GetComponentStatusOutput getComponentStatus(GetComponentStatusInput inp)
    {
        GetComponentStatus getComponentStatus = new GetComponentStatus(monitor2Service, inp, upnpService.getControlPoint());
        GetComponentStatusOutput res = getComponentStatus.executeAction();
        return res;        
    }

    public GetAllContextStatusOutput getAllContextStatus()
    {
        GetAllContextStatus getAllContextStatus = new GetAllContextStatus(monitor2Service,  upnpService.getControlPoint());
        GetAllContextStatusOutput res = getAllContextStatus.executeAction();
        return res;        
    }

    public GetErrorInfoOutput getErrorInfo()
    {
        GetErrorInfo getErrorInfo = new GetErrorInfo(monitor2Service,  upnpService.getControlPoint());
        GetErrorInfoOutput res = getErrorInfo.executeAction();
        return res;        
    }

    public GetComponentContextStatusOutput getComponentContextStatus(GetComponentContextStatusInput inp)
    {
        GetComponentContextStatus getComponentContextStatus = new GetComponentContextStatus(monitor2Service, inp, upnpService.getControlPoint());
        GetComponentContextStatusOutput res = getComponentContextStatus.executeAction();
        return res;        
    }

    public void undoPackage(UndoPackageInput inp)
    {
        UndoPackage undoPackage = new UndoPackage(monitor2Service, inp, upnpService.getControlPoint());
        undoPackage.executeAction();
    }

    public GetContextStatusOutput getContextStatus(GetContextStatusInput inp)
    {
        GetContextStatus getContextStatus = new GetContextStatus(monitor2Service, inp, upnpService.getControlPoint());
        GetContextStatusOutput res = getContextStatus.executeAction();
        return res;        
    }

    public GetAboutInfoOutput getAboutInfo()
    {
        GetAboutInfo getAboutInfo = new GetAboutInfo(monitor2Service,  upnpService.getControlPoint());
        GetAboutInfoOutput res = getAboutInfo.executeAction();
        return res;        
    }

    public GetAllModuleStatusOutput getAllModuleStatus()
    {
        GetAllModuleStatus getAllModuleStatus = new GetAllModuleStatus(monitor2Service,  upnpService.getControlPoint());
        GetAllModuleStatusOutput res = getAllModuleStatus.executeAction();
        return res;        
    }

    public void stopContext(StopContextInput inp)
    {
        StopContext stopContext = new StopContext(monitor2Service, inp, upnpService.getControlPoint());
        stopContext.executeAction();
    }

    public GetPackageAboutInfoOutput getPackageAboutInfo(GetPackageAboutInfoInput inp)
    {
        GetPackageAboutInfo getPackageAboutInfo = new GetPackageAboutInfo(monitor2Service, inp, upnpService.getControlPoint());
        GetPackageAboutInfoOutput res = getPackageAboutInfo.executeAction();
        return res;        
    }

    public GetAllPackageStatusOutput getAllPackageStatus()
    {
        GetAllPackageStatus getAllPackageStatus = new GetAllPackageStatus(monitor2Service,  upnpService.getControlPoint());
        GetAllPackageStatusOutput res = getAllPackageStatus.executeAction();
        return res;        
    }

    public void checkPackageLicenseType(CheckPackageLicenseTypeInput inp)
    {
        CheckPackageLicenseType checkPackageLicenseType = new CheckPackageLicenseType(monitor2Service, inp, upnpService.getControlPoint());
        checkPackageLicenseType.executeAction();
    }

    public void checkPackage(CheckPackageInput inp)
    {
        CheckPackage checkPackage = new CheckPackage(monitor2Service, inp, upnpService.getControlPoint());
        checkPackage.executeAction();
    }

    public GetHostAndPortOutput getHostAndPort()
    {
        GetHostAndPort getHostAndPort = new GetHostAndPort(monitor2Service,  upnpService.getControlPoint());
        GetHostAndPortOutput res = getHostAndPort.executeAction();
        return res;        
    }

    public void startContext(StartContextInput inp)
    {
        StartContext startContext = new StartContext(monitor2Service, inp, upnpService.getControlPoint());
        startContext.executeAction();
    }

    public GetAllPackagePropertiesOutput getAllPackageProperties(GetAllPackagePropertiesInput inp)
    {
        GetAllPackageProperties getAllPackageProperties = new GetAllPackageProperties(monitor2Service, inp, upnpService.getControlPoint());
        GetAllPackagePropertiesOutput res = getAllPackageProperties.executeAction();
        return res;        
    }

    public GetPropertyOutput getProperty(GetPropertyInput inp)
    {
        GetProperty getProperty = new GetProperty(monitor2Service, inp, upnpService.getControlPoint());
        GetPropertyOutput res = getProperty.executeAction();
        return res;        
    }

    public GetPackageStatusOutput getPackageStatus(GetPackageStatusInput inp)
    {
        GetPackageStatus getPackageStatus = new GetPackageStatus(monitor2Service, inp, upnpService.getControlPoint());
        GetPackageStatusOutput res = getPackageStatus.executeAction();
        return res;        
    }

    public void relaunchRuntime()
    {
        RelaunchRuntime relaunchRuntime = new RelaunchRuntime(monitor2Service,  upnpService.getControlPoint());
        relaunchRuntime.executeAction();
    }

    public GetContextComponentStatusListOutput getContextComponentStatusList(GetContextComponentStatusListInput inp)
    {
        GetContextComponentStatusList getContextComponentStatusList = new GetContextComponentStatusList(monitor2Service, inp, upnpService.getControlPoint());
        GetContextComponentStatusListOutput res = getContextComponentStatusList.executeAction();
        return res;        
    }

    public SetPropertyOutput setProperty(SetPropertyInput inp)
    {
        SetProperty setProperty = new SetProperty(monitor2Service, inp, upnpService.getControlPoint());
        SetPropertyOutput res = setProperty.executeAction();
        return res;        
    }

    public void replacePackage(ReplacePackageInput inp)
    {
        ReplacePackage replacePackage = new ReplacePackage(monitor2Service, inp, upnpService.getControlPoint());
        replacePackage.executeAction();
    }
}
