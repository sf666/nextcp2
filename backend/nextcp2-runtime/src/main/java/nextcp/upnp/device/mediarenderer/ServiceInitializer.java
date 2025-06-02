package nextcp.upnp.device.mediarenderer;

import java.util.List;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.dto.MediaRendererServicesDto;
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
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.RenderingControlService;

/**
 * Initialized media renderer services.
 */
public class ServiceInitializer
{
    private static final Logger log = LoggerFactory.getLogger(ServiceInitializer.class.getName());

    static final String UPNP_SCHEMA = "schemas-upnp-org";
    static final String UPNP_AVTransport = "AVTransport";
    static final String UPNP_RenderingControl = "RenderingControl";
    static final String UPNP_ConnectionManager = "ConnectionManager";

    static final String OH_SCHEMA = "av-openhome-org";
    static final String OH_Info = "Info";
    static final String OH_Time = "Time";
    static final String OH_Volume = "Volume";
    static final String OH_Credentials = "Credentials";
    static final String OH_Playlist = "Playlist";
    static final String OH_Radio = "Radio";
    static final String OH_Product = "Product";
    static final String OH_Transport = "Transport";
    static final String OH_OAuth = "OAuth";
    static final String OH_Receiver = "Receiver";

    void initializeServices(UpnpService upnpService, RemoteDevice device, MediaRendererDevice renderer, List<MediaRendererServicesDto> services)
    {
        log.info("[initializeServices] : adding services for device {} ", renderer.getFriendlyName());
        for (RemoteService service : device.getServices())
        {
            MediaRendererServicesDto serviceDto = new MediaRendererServicesDto(service.getServiceType().getNamespace(), service.getServiceType().toFriendlyString(),
                    "" + service.getServiceType().getVersion());
            log.info(serviceDto.toString());
            services.add(serviceDto);

            if (device.hasEmbeddedDevices())
            {
                log.warn("Device has embedded devices.");
            }
            if (service.getServiceType().getNamespace().equalsIgnoreCase(UPNP_SCHEMA))
            {
                if (service.getServiceType().getType().equalsIgnoreCase(UPNP_AVTransport))
                {
                    renderer.upnp_avTransportService = new AVTransportService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(UPNP_RenderingControl))
                {
                    renderer.upnp_renderingControlService = new RenderingControlService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(UPNP_ConnectionManager))
                {
                    renderer.upnp_connectionManagerService = new ConnectionManagerService(upnpService, device);
                }
                else
                {
                    log.warn("Device has unknown upnp services : " + service.getServiceType().getType());
                }

            }
            else if (service.getServiceType().getNamespace().equalsIgnoreCase(OH_SCHEMA))
            {
                if (service.getServiceType().getType().equalsIgnoreCase(OH_Info))
                {
                    renderer.oh_infoService = new InfoService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Time))
                {
                    renderer.oh_timeService = new TimeService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Volume))
                {
                    renderer.oh_volumeService = new VolumeService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Credentials))
                {
                    renderer.oh_credentialsService = new CredentialsService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Playlist))
                {
                    renderer.oh_playlistService = new PlaylistService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Radio))
                {
                    renderer.oh_radioService = new RadioService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Product))
                {
                    renderer.oh_productService = new ProductService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Transport))
                {
                    renderer.oh_transportService = new TransportService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_OAuth))
                {
                	log.debug("OAuth not implemented");
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Receiver))
                {
                	log.debug("Receiver not implemented");
                }
                else
                {
                    log.warn("Device has unknown openhome services : " + service.getServiceType().getType());
                }
            }
            else
            {
                log.warn("Device has unknown services in namespace : " + service.getServiceType().getNamespace());
            }
        }
    }

    void renewServices(UpnpService upnpService, RemoteDevice device, MediaRendererDevice renderer, List<MediaRendererServicesDto> services)
    {
        log.info("[renewServices] : for device " + renderer.getFriendlyName());
        for (RemoteService service : device.getServices())
        {
            MediaRendererServicesDto serviceDto = new MediaRendererServicesDto(service.getServiceType().getNamespace(), service.getServiceType().toFriendlyString(),
                    "" + service.getServiceType().getVersion());
            log.info(serviceDto.toString());
            services.add(serviceDto);

            if (device.hasEmbeddedDevices())
            {
                log.warn("Device has embedded devices.");
            }
            if (service.getServiceType().getNamespace().equalsIgnoreCase(UPNP_SCHEMA))
            {
                if (service.getServiceType().getType().equalsIgnoreCase(UPNP_AVTransport))
                {
                    renderer.upnp_avTransportService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(UPNP_RenderingControl))
                {
                    renderer.upnp_renderingControlService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(UPNP_ConnectionManager))
                {
                    renderer.upnp_connectionManagerService.renewService(upnpService, device);
                }
                else
                {
                    log.warn("Device has unknown upnp services : " + service.getServiceType().getType());
                }

            }
            else if (service.getServiceType().getNamespace().equalsIgnoreCase(OH_SCHEMA))
            {
                if (service.getServiceType().getType().equalsIgnoreCase(OH_Info))
                {
                    renderer.oh_infoService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Time))
                {
                    renderer.oh_timeService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Volume))
                {
                    renderer.oh_volumeService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Credentials))
                {
                    renderer.oh_credentialsService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Playlist))
                {
                    renderer.oh_playlistService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Radio))
                {
                    renderer.oh_radioService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Product))
                {
                    renderer.oh_productService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Transport))
                {
                    renderer.oh_transportService.renewService(upnpService, device);
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_OAuth))
                {
                	log.debug("OAuth not implemented");
                }
                else if (service.getServiceType().getType().equalsIgnoreCase(OH_Receiver))
                {
                	log.debug("Receiver not implemented");
                }                
                else
                {
                    log.warn("Device has unknown openhome services : " + service.getServiceType().getType());
                }
            }
            else
            {
                log.warn("Device has unknown services in namespace : " + service.getServiceType().getNamespace());
            }
        }
    }
}
