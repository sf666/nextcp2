package nextcp.upnp.device.mediarenderer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.jupnp.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.config.RendererConfig;
import nextcp.domainmodel.device.services.IInfoService;
import nextcp.domainmodel.device.services.IPlaylistService;
import nextcp.domainmodel.device.services.IProductService;
import nextcp.domainmodel.device.services.IRadioService;
import nextcp.domainmodel.device.services.ITransport;
import nextcp.dto.DeviceDriverState;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaRendererServicesDto;
import nextcp.dto.RendererDeviceConfiguration;
import nextcp.dto.ToastrMessage;
import nextcp.dto.TrackInfoDto;
import nextcp.dto.TrackTimeDto;
import nextcp.dto.TransportServiceStateDto;
import nextcp.service.ISchedulerService;
import nextcp.service.SchedulerService;
import nextcp.upnp.device.BaseDevice;
import nextcp.upnp.device.mediarenderer.avtransport.AvTransportEventListener;
import nextcp.upnp.device.mediarenderer.avtransport.AvTransportEventPublisher;
import nextcp.upnp.device.mediarenderer.avtransport.Upnp_AVTransportBridge;
import nextcp.upnp.device.mediarenderer.ohinfo.OhInfoServiceEventListener;
import nextcp.upnp.device.mediarenderer.ohinfo.Oh_InfoServiceImpl;
import nextcp.upnp.device.mediarenderer.ohradio.OhRadioBridge;
import nextcp.upnp.device.mediarenderer.ohradio.OhRadioServiceEventListener;
import nextcp.upnp.device.mediarenderer.ohtime.OhTimeServiceEventListener;
import nextcp.upnp.device.mediarenderer.ohtransport.OhTransportBridge;
import nextcp.upnp.device.mediarenderer.ohtransport.OhTransportEventListener;
import nextcp.upnp.device.mediarenderer.playlist.CpPlaylistService;
import nextcp.upnp.device.mediarenderer.playlist.OhPlaylistBridge;
import nextcp.upnp.device.mediarenderer.playlist.OhPlaylistServiceEventListener;
import nextcp.upnp.device.mediarenderer.product.OhProductServiceBridge;
import nextcp.upnp.device.mediarenderer.product.OhProductServiceEventListener;
import nextcp.upnp.device.mediarenderer.renderingControl.RenderingControlEventListener;
import nextcp.upnp.device.mediarenderer.volume.OhVolumeServiceEventListener;
import nextcp.upnp.modelGen.avopenhomeorg.credentials1.CredentialsService;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoService;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.PlaylistService;
import nextcp.upnp.modelGen.avopenhomeorg.product1.ProductService;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.RadioService;
import nextcp.upnp.modelGen.avopenhomeorg.time1.TimeService;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.TransportService;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.VolumeService;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.AVTransportService;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager1.ConnectionManagerService;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.RenderingControlService;
import nextcp.util.BackendException;

/**
 * This class represents s device and it's capabilities.
 */
public class MediaRendererDevice extends BaseDevice implements ISchedulerService
{
    private static final Logger log = LoggerFactory.getLogger(MediaRendererDevice.class.getName());

    @Autowired
    private MediaRendererFactories factories = null;

    @Autowired
    private RendererConfig rendererConfigService = null;

    private RendererDeviceConfiguration rendererConfig = null;

    @Autowired
    private SchedulerService schedulerService = null;

    @Autowired
    private ApplicationEventPublisher eventPublisher = null;

    private IDeviceDriver deviceDriver = null;

    private List<MediaRendererServicesDto> services = new ArrayList<>();

    // private IDeviceDriverService
    private ServiceInitializer serviceInitializer = new ServiceInitializer();

    // Event Listener for services. Service state variable is held here.
    protected AvTransportEventListener avTransportEventListener = null;
    protected AvTransportEventPublisher avTransportEventPublisher = null;
    protected RenderingControlEventListener renderingControlEventListener = null;
    protected OhInfoServiceEventListener ohInfoServiceEventListener = null;
    protected OhTimeServiceEventListener ohTimeServiceEventListener = null;
    protected OhRadioServiceEventListener ohRadioServiceEventListener = null;
    protected OhProductServiceEventListener ohProductServiceEventListener = null;
    protected OhVolumeServiceEventListener ohVolumeServiceEventListener = null;
    protected OhTransportEventListener ohTransportEventListener = null;
    protected OhPlaylistServiceEventListener ohPlaylistServiceEventListener = null;

    // upnp
    protected AVTransportService upnp_avTransportService = null;
    protected RenderingControlService upnp_renderingControlService = null;
    protected ConnectionManagerService upnp_connectionManagerService = null;
    protected UpnpDeviceDriver upnpDeviceDriver = null;

    // upnp wrapper
    protected Upnp_AVTransportBridge avTransportBridge = null;

    // openhome services
    protected InfoService oh_infoService = null;
    protected TimeService oh_timeService = null;
    protected VolumeService oh_volumeService = null;
    protected CredentialsService oh_credentialsService = null;
    protected PlaylistService oh_playlistService = null;
    protected RadioService oh_radioService = null;
    protected ProductService oh_productService = null;
    protected TransportService oh_transportService = null;
    protected OpenHomeDeviceDriver ohDeviceDriver = null;

    // Bridges to openhome services above
    protected OhRadioBridge oh_radioBridge = null;
    protected OhPlaylistBridge oh_playlistBridge = null;

    // Delegate services to generated models
    protected ITransport transportBridge = null;
    protected IPlaylistService playlistService = null;
    protected IRadioService radioService = null;
    protected IProductService productService = null;
    protected IInfoService infoService = null;

    public MediaRendererDevice(RemoteDevice device)
    {
        super(device);

    }

    @PostConstruct
    protected void init()
    {
        initServices();

        if (hasUpnpAvTransport())
        {
            avTransportBridge = new Upnp_AVTransportBridge(upnp_avTransportService, this);
            avTransportEventListener = new AvTransportEventListener(this);
            upnp_avTransportService.addSubscriptionEventListener(avTransportEventListener);
            avTransportEventPublisher = new AvTransportEventPublisher(this);
            avTransportEventListener.addEventListener(avTransportEventPublisher);
            avTransportEventListener.addEventListener(avTransportBridge);
        }
        else
        {
            log.info("Device doesn't support AVTransportService : " + getFriendlyName());
        }

        if (hasOhInfoService())
        {
            ohInfoServiceEventListener = new OhInfoServiceEventListener(this);
            oh_infoService.addSubscriptionEventListener(ohInfoServiceEventListener);
            this.infoService = new Oh_InfoServiceImpl(this, oh_infoService);
        }
        else
        {
            // Extract Info's from AVTransport ...
            this.infoService = avTransportBridge;
            schedulerService.addNotifier(this);
        }

        if (hasOhVolumeService())
        {
            ohVolumeServiceEventListener = new OhVolumeServiceEventListener();
            oh_volumeService.addSubscriptionEventListener(ohVolumeServiceEventListener);
        }

        if (hasUpnpRenderingControlService())
        {
            renderingControlEventListener = new RenderingControlEventListener();
            upnp_renderingControlService.addSubscriptionEventListener(renderingControlEventListener);
        }

        if (hasOhTimeService())
        {
            ohTimeServiceEventListener = new OhTimeServiceEventListener(getEventPublisher(), this);
            oh_timeService.addSubscriptionEventListener(ohTimeServiceEventListener);
        }
        else
        {
            schedulerService.addNotifier(this);
        }

        if (hasOhPlaylistService())
        {
            oh_playlistBridge = new OhPlaylistBridge(oh_playlistService, getDtoBuilder(), this);
            OhPlaylistBridge ohPlaylist = oh_playlistBridge;
            ohPlaylistServiceEventListener = new OhPlaylistServiceEventListener(ohPlaylist, this);
            oh_playlistService.addSubscriptionEventListener(ohPlaylistServiceEventListener);
            playlistService = ohPlaylist;
        }
        else
        {
            // no OH servies. Playlist will be internally controlled by this control point.
            if (avTransportEventListener != null && hasUpnpAvTransport())
            {
                CpPlaylistService playlist = new CpPlaylistService(this);
                playlistService = playlist;
                avTransportEventListener.addEventListener(playlist);
            }
        }

        if (hasProductService())
        {
            ohProductServiceEventListener = new OhProductServiceEventListener(getEventPublisher(), this);
            oh_productService.addSubscriptionEventListener(ohProductServiceEventListener);
            productService = new OhProductServiceBridge(oh_productService, ohProductServiceEventListener, getDtoBuilder());
        }

        if (hasRadioService())
        {
            oh_radioBridge = new OhRadioBridge(oh_radioService, getDtoBuilder(), this);
            radioService = oh_radioBridge;
            ohRadioServiceEventListener = new OhRadioServiceEventListener(this);
            oh_radioService.addSubscriptionEventListener(ohRadioServiceEventListener);
        }

        //
        // 2nd step: Identify available services and glue correct bridges together
        //

        // transport bridge (transport state publishing) ...
        if (hasOhTransport())
        {
            ohTransportEventListener = new OhTransportEventListener(this);
            oh_transportService.addSubscriptionEventListener(ohTransportEventListener);
            ohTransportEventListener.setShouldPublishTransportServiceState(true);
            transportBridge = new OhTransportBridge(this, oh_transportService, ohTransportEventListener, eventPublisher);

            // In case we have an OhTransport service, disable sending AVTransport
            avTransportEventPublisher.setShouldPublishTransportServiceState(false);
        }
        else
        {
            transportBridge = avTransportBridge;
            avTransportEventPublisher.setShouldPublishTransportServiceState(true);
        }

        // must be called after OH Services!
        updateDeviceDriver();
    }

    public void updateDeviceDriver()
    {
        deviceDriver = createDeviceDriver();
    }

    private void initServices()
    {
        updateRendererConfig();
        serviceInitializer.initializeServices(getUpnpService(), getDevice(), this, services);
        rendererConfig.mediaRenderer = getAsDto();
        rendererConfigService.updateRendererDevice(rendererConfig);
    }

    private void updateRendererConfig()
    {
        rendererConfig = rendererConfigService.getMediaRendererConfig(getUDN().getIdentifierString());
        if (rendererConfig == null)
        {
            rendererConfig = rendererConfigService.addMediaRendererDeviceConfig(this);
        }
    }

    public List<MediaRendererServicesDto> getServices()
    {
        return services;
    }

    private boolean hasOhVolumeService()
    {
        return oh_volumeService != null;
    }

    /**
     * <pre>
     * The streamer connected to the physical device driver (amplifier / receiver) is OpenHome compatible. 
     * 
     * Attention: Openhome product & volume service must be initialized first!
     * </pre>
     * 
     * @return
     */
    private IDeviceDriver getOhDeviceDriver(IDeviceDriver physicalDeviceDriver)
    {
        if (productService == null)
        {
            log.warn("Product service is not initialized ... OH device driver is not being created for device " + getFriendlyName());
            return null;
        }
        if (oh_volumeService == null)
        {
            log.warn("Volume service is not initialized ... OH device driver is not being created." + getFriendlyName());
            return null;
        }

        if (ohDeviceDriver == null)
        {
            ohDeviceDriver = new OpenHomeDeviceDriver(this, getEventPublisher(), productService, oh_volumeService, physicalDeviceDriver,
                    rendererConfig.setCoveredUpnpDeviceToMaxVolume);
            log.info("Added OpenHome Implementation for volume and power control to device " + getFriendlyName());
            if (ohProductServiceEventListener != null)
            {
                ohProductServiceEventListener.addStandbyCallback(ohDeviceDriver);
            }
            else
            {
                log.warn("no ohProductServiceEventListener available. No standby updates will be available for device " + getFriendlyName());
            }
            if (ohVolumeServiceEventListener != null)
            {
                ohVolumeServiceEventListener.addDeviceDriverCallback(ohDeviceDriver);
            }
            else
            {
                log.warn("no ohVolumeServiceEventListener available. No volume updates will be available.");
            }
        }
        return ohDeviceDriver;
    }

    /**
     * The streamer connected to the physical device driver (amplifier / receiver) has an UPnP AV implementation.
     * 
     * @param physicalDeviceDriver
     * @return
     */
    private IDeviceDriver getUpnpDeviceDriver(IDeviceDriver physicalDeviceDriver)
    {
        if (upnp_renderingControlService == null)
        {
            log.warn("Rendering Control Service is not initialized ... UPnP device driver is not being created.");
            return null;
        }

        if (upnpDeviceDriver == null)
        {
            upnpDeviceDriver = new UpnpDeviceDriver(this, getEventPublisher(), upnp_renderingControlService, physicalDeviceDriver, rendererConfig.setCoveredUpnpDeviceToMaxVolume);
            log.info("Using UPnP Implementation for volume control for device " + getFriendlyName());

            if (renderingControlEventListener != null)
            {
                renderingControlEventListener.addDeviceDriverCallback(upnpDeviceDriver);
            }
            else
            {
                log.warn("no renderingControlEventListener available. No volume updates will be available.");
            }
        }
        return upnpDeviceDriver;
    }

    public boolean hasRadioService()
    {
        return oh_radioService != null;
    }

    public boolean hasOhPlaylistService()
    {
        // return false;
        return oh_playlistService != null;
    }

    public boolean hasOhTimeService()
    {
        return oh_timeService != null;
    }

    public boolean hasOhInfoService()
    {
        return oh_infoService != null;
    }

    public boolean hasOhRadioService()
    {
        return oh_radioService != null;
    }

    protected IDeviceDriver getDeviceDriver()
    {
        return deviceDriver;
    }

    /**
     * Create or recreate device driver. Can be called, after configuration has changed.
     */
    private IDeviceDriver createDeviceDriver()
    {
        updateRendererConfig();
        IDeviceDriver dd = null;
        IDeviceDriver physicalDriver = null;

        if (rendererConfig != null && !StringUtils.isBlank(rendererConfig.deviceDriverType))
        {
            physicalDriver = factories.createDeviceDriver(getUdnAsString(), rendererConfig.deviceDriverType, rendererConfig.connectionString);
            log.info(String.format("External AV Device Driver created of type '%s' for device '%s'. Connection string : %s", rendererConfig.deviceDriverType, getFriendlyName(),
                    rendererConfig.connectionString));
        }
        dd = getOhDeviceDriver(physicalDriver);
        if (dd == null)
        {
            dd = getUpnpDeviceDriver(physicalDriver);
        }
        if (dd == null)
        {
            log.info("Attention: No device driver configured for device : " + getFriendlyName());
        }
        return dd;
    }

    public AvTransportEventPublisher getAvTransportEventPublisher()
    {
        if (avTransportEventPublisher == null)
        {
            throw new BackendException(BackendException.SERVICE_UNAVAILABLE_AVTRANSPORT, "AV Transport Service unavailable");
        }
        return avTransportEventPublisher;
    }

    public AvTransportEventListener getAvTransportEventListener()
    {
        if (avTransportEventListener == null)
        {
            throw new BackendException(BackendException.SERVICE_UNAVAILABLE_AVTRANSPORT, "AV Transport Service unavailable");
        }
        return avTransportEventListener;
    }

    public boolean hasDeviceDriver()
    {
        return deviceDriver != null;
    }

    public boolean hasUpnpAvTransport()
    {
        return upnp_avTransportService != null;
    }

    public boolean hasOhTransport()
    {
        return oh_transportService != null;
    }

    public boolean hasUpnpRenderingControlService()
    {
        return upnp_renderingControlService != null;
    }

    public boolean hasProductService()
    {
        return oh_productService != null;
    }

    public MediaRendererDto getAsDto()
    {
        if (productService != null)
        {
            return new MediaRendererDto(getUDN().getIdentifierString(), getFriendlyName(), services, productService.getCurrentInputSource(), productService.getSourceList());
        }
        return new MediaRendererDto(getUDN().getIdentifierString(), getFriendlyName(), services, null, null);
    }

    //
    // Action delegates
    // ===========================================================

    public IPlaylistService getPlaylistServiceBridge()
    {
        return playlistService;
    }

    public ITransport getTransportServiceBridge()
    {
        return transportBridge;
    }

    public Upnp_AVTransportBridge getAvTransportBridge()
    {
        return avTransportBridge;
    }

    public IRadioService getRadioServiceBridge()
    {
        return radioService;
    }

    public IProductService getProductService()
    {
        return productService;
    }

    public TrackInfoDto getTrackInfo()
    {
        TrackInfoDto ti = infoService.getTrackinfo();
        ti.mediaRendererUdn = getUDN().getIdentifierString();
        return ti;
    }

    /**
     * 
     */
    @Override
    public void tick(long counter)
    {
        if (transportBridge != null && !hasOhInfoService() && transportIsPlaying())
        {
            TrackTimeDto dto = avTransportBridge.generateTractTimeDto();
            eventPublisher.publishEvent(dto);
        }
    }

    private boolean transportIsPlaying()
    {
        if (transportBridge == null)
        {
            return false;
        }
        TransportServiceStateDto state = transportBridge.getCurrentTransportServiceState();

        return "PLAYING".equals(state.transportState);
    }

    // Device operations

    public void setVolume(int vol)
    {
        if (getDeviceDriver() == null)
        {
            eventPublisher.publishEvent(new ToastrMessage(null, "error", "Volume", "no device driver available"));
            return;
        }
        getDeviceDriver().setVolume(vol);
    }

    public void setInput(String input)
    {
        if (getDeviceDriver() == null)
        {
            eventPublisher.publishEvent(new ToastrMessage(null, "error", "Volume", "no device driver available"));
            return;
        }
        getDeviceDriver().setInput(input);
    }

    public DeviceDriverState getDeviceDriverState()
    {
        return getDeviceDriver().getDeviceDriverState();
    }

    public void setStandby(boolean standbyState)
    {
        getDeviceDriver().setStandby(standbyState);
    }

    public boolean getStandby()
    {
        return getDeviceDriver().getStandby();
    }

    @Override
    public String toString()
    {
        return "MediaRenderer : " + getFriendlyName();
    }
}
