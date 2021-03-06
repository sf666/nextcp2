package nextcp.upnp.device.mediarenderer;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    void initializeServices(UpnpService upnpService, RemoteDevice device, MediaRendererDevice renderer)
    {
        for (RemoteService service : device.getServices())
        {
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
