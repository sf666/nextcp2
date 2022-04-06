package nextcp.upnp.device.mediarenderer;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.config.RendererConfig;
import nextcp.domainmodel.device.services.IInfoService;
import nextcp.domainmodel.device.services.IPlaylistService;
import nextcp.domainmodel.device.services.IProductService;
import nextcp.domainmodel.device.services.IRadioService;
import nextcp.domainmodel.device.services.IUpnpAvTransport;
import nextcp.dto.DeviceDriverState;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.RendererDeviceConfiguration;
import nextcp.dto.ToastrMessage;
import nextcp.dto.TrackInfoDto;
import nextcp.dto.TrackTimeDto;
import nextcp.service.ISchedulerService;
import nextcp.service.SchedulerService;
import nextcp.upnp.device.BaseDevice;
import nextcp.upnp.device.mediarenderer.avtransport.AvTransportEventListener;
import nextcp.upnp.device.mediarenderer.avtransport.AvTransportEventPublisher;
import nextcp.upnp.device.mediarenderer.avtransport.AvTransportState;
import nextcp.upnp.device.mediarenderer.avtransport.Upnp_AVTransportBridge;
import nextcp.upnp.device.mediarenderer.ohinfo.OhInfoServiceEventListener;
import nextcp.upnp.device.mediarenderer.ohinfo.Oh_InfoServiceImpl;
import nextcp.upnp.device.mediarenderer.ohradio.OhRadioBridge;
import nextcp.upnp.device.mediarenderer.ohtime.OhTimeServiceEventListener;
import nextcp.upnp.device.mediarenderer.playlist.CpPlaylistService;
import nextcp.upnp.device.mediarenderer.playlist.OhPlaylist;
import nextcp.upnp.device.mediarenderer.playlist.OhPlaylistServiceEventListener;
import nextcp.upnp.device.mediarenderer.product.OhProductServiceBridge;
import nextcp.upnp.device.mediarenderer.product.OhProductServiceEventListener;
import nextcp.upnp.device.mediarenderer.renderingControl.RenderingControlEventListener;
import nextcp.upnp.device.mediarenderer.volume.OhVolumeServiceEventListener;
import nextcp.upnp.modelGen.avopenhomeorg.credentials.CredentialsService;
import nextcp.upnp.modelGen.avopenhomeorg.info.InfoService;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.PlaylistService;
import nextcp.upnp.modelGen.avopenhomeorg.product.ProductService;
import nextcp.upnp.modelGen.avopenhomeorg.radio.RadioService;
import nextcp.upnp.modelGen.avopenhomeorg.time.TimeService;
import nextcp.upnp.modelGen.avopenhomeorg.volume.VolumeService;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.AVTransportService;
import nextcp.upnp.modelGen.schemasupnporg.connectionManager.ConnectionManagerService;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.RenderingControlService;
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

    // private IDeviceDriverService
    private ServiceInitializer serviceInitializer = new ServiceInitializer();

    // Event Listener for services. Service state variable is held here.
    private AvTransportEventListener avTransportEventListener = null;
    private AvTransportEventPublisher avTransportEventPublisher = null;
    private RenderingControlEventListener renderingControlEventListener = null;
    private OhInfoServiceEventListener ohInfoServiceEventListener = null;
    private OhTimeServiceEventListener ohTimeServiceEventListener = null;
    private OhProductServiceEventListener ohProductServiceEventListener = null;
    private OhVolumeServiceEventListener ohVolumeServiceEventListener = null;

    // upnp
    AVTransportService upnp_avTransportService = null;
    RenderingControlService upnp_renderingControlService = null;
    ConnectionManagerService upnp_connectionManagerService = null;
    UpnpDeviceDriver upnpDeviceDriver = null;

    // openhome services
    InfoService oh_infoService = null;
    TimeService oh_timeService = null;
    VolumeService oh_volumeService = null;
    CredentialsService oh_credentialsService = null;
    PlaylistService oh_playlistService = null;
    RadioService oh_radioService = null;
    ProductService oh_productService = null;
    OpenHomeDeviceDriver ohDeviceDriver = null;

    // Delegate services to generated models
    Upnp_AVTransportBridge avTransportBridge = null;
    IPlaylistService playlistService = null;
    IRadioService radioService = null;
    IProductService productService = null;
    IInfoService infoService = null;

    public MediaRendererDevice(RemoteDevice device)
    {
        super(device);

    }

    @PostConstruct
    private void init()
    {
        rendererConfig = rendererConfigService.getMediaRendererConfig(getUDN().getIdentifierString());
        // ATTENTION: Initialize services first
        serviceInitializer.initializeServices(getUpnpService(), getDevice(), this);

        if (hasUpnpAvTransport())
        {
            avTransportBridge = new Upnp_AVTransportBridge(upnp_avTransportService, this);
            avTransportEventListener = new AvTransportEventListener(this);
            avTransportEventPublisher = new AvTransportEventPublisher(this);
            avTransportEventListener.addEventListener(avTransportEventPublisher);
            upnp_avTransportService.addSubscriptionEventListener(avTransportEventListener);
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

        if (hasOhPlaylistService())
        {
            OhPlaylist ohPlaylist = new OhPlaylist(oh_playlistService, getDtoBuilder());
            oh_playlistService.addSubscriptionEventListener(new OhPlaylistServiceEventListener(getEventPublisher(), ohPlaylist, this));
            playlistService = ohPlaylist;
        }
        else
        {
            // no OH servies. Playlist will be internally controlled by this control point.
            if (hasUpnpAvTransport())
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
            OhProductServiceBridge productService = new OhProductServiceBridge(oh_productService, ohProductServiceEventListener, getDtoBuilder());
            this.productService = productService;
        }

        if (hasRadioService())
        {
            radioService = new OhRadioBridge(oh_radioService, getDtoBuilder());
        }

        // must be called after OH Services!
        deviceDriver = createDeviceDriver();
    }

    private boolean hasOhVolumeService()
    {
        return oh_volumeService != null;
    }

    /**
     * Attention: Openhome product & volume service must be initialized first!
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
        return ohDeviceDriver;
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

    protected IDeviceDriver getDeviceDriver()
    {
        return deviceDriver;
    }

    private IDeviceDriver createDeviceDriver()
    {
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
        return new MediaRendererDto(getUDN().getIdentifierString(), getFriendlyName());
    }

    //
    // Action delegates
    // ===========================================================

    public IPlaylistService getPlaylistServiceBridge()
    {
        return playlistService;
    }

    public IUpnpAvTransport getAvTransportServiceBridge()
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
        if (!hasOhInfoService() && avTransportIsPlaying())
        {
            TrackTimeDto dto = avTransportBridge.generateTractTimeDto();
            eventPublisher.publishEvent(dto);
        }
    }

    private boolean avTransportIsPlaying()
    {
        AvTransportState state = avTransportEventPublisher.getCurrentAvTransportState();
        return state.TransportState.equals("PLAYING");
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
}
