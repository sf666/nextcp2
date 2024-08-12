package nextcp2.upnp.localdevice;

import org.jupnp.binding.LocalServiceBinder;
import org.jupnp.binding.annotations.AnnotationLocalServiceBinder;
import org.jupnp.model.DefaultServiceManager;
import org.jupnp.model.ServiceManager;
import org.jupnp.model.ValidationException;
import org.jupnp.model.meta.DeviceDetails;
import org.jupnp.model.meta.DeviceIdentity;
import org.jupnp.model.meta.Icon;
import org.jupnp.model.meta.LocalDevice;
import org.jupnp.model.meta.LocalService;
import org.jupnp.model.meta.ManufacturerDetails;
import org.jupnp.model.meta.ModelDetails;
import org.jupnp.model.types.UDADeviceType;
import org.jupnp.model.types.UDN;
import org.jupnp.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.jupnp.support.connectionmanager.ConnectionManagerService;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.lastchange.LastChangeAwareServiceManager;
import org.jupnp.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;

public class Nextcp2Renderer {

    public static final long LAST_CHANGE_FIRING_INTERVAL_MILLISECONDS = 500;
    
    final protected LocalServiceBinder binder = new AnnotationLocalServiceBinder();

    protected LastChange avTransportLastChange = null;
    protected LastChange renderingControlLastChange = null;


    final protected ServiceManager<ConnectionManagerService> connectionManager;
    final protected LastChangeAwareServiceManager<Nextcp2AvTransportService> avTransport;
    final protected LastChangeAwareServiceManager<Nextcp2AudioRenderingControl> renderingControl;

    final protected LocalDevice device;
    
	
	public LocalDevice getLocalDevice() {
		return device;
	}

	public Nextcp2Renderer() {
		AVTransportLastChangeParser parser = new AVTransportLastChangeParser();
	    avTransportLastChange = new LastChange(parser);
	    RenderingControlLastChangeParser rcParser = new RenderingControlLastChangeParser();
	    renderingControlLastChange = new LastChange(rcParser);
		
		LocalService<Nextcp2AudioRenderingControl> renderingControlService = binder.read(Nextcp2AudioRenderingControl.class);
		renderingControl =
		        new LastChangeAwareServiceManager<Nextcp2AudioRenderingControl>(
		                renderingControlService,
		                new RenderingControlLastChangeParser()
		        ) {
		            @Override
		            protected Nextcp2AudioRenderingControl createServiceInstance() throws Exception {
		                return new Nextcp2AudioRenderingControl(renderingControlLastChange);
		            }
		        };
		renderingControlService.setManager(renderingControl);

		
		 // The connection manager doesn't have to do much, HTTP is stateless
        LocalService connectionManagerService = binder.read(ConnectionManagerService.class);
        connectionManager =
                new DefaultServiceManager(connectionManagerService) {
                    @Override
                    protected Object createServiceInstance() throws Exception {
                        return new ConnectionManagerService();
                    }
                };
        connectionManagerService.setManager(connectionManager);
        
        
        
		LocalService<Nextcp2AvTransportService> avTransportService = binder.read(Nextcp2AvTransportService.class);
		avTransport =
		        new LastChangeAwareServiceManager<Nextcp2AvTransportService>(
		                avTransportService,
		                new AVTransportLastChangeParser()
		        ) {
		            @Override
		            protected Nextcp2AvTransportService createServiceInstance() throws Exception {
		                return new Nextcp2AvTransportService(); // avTransportLastChange
		            }
		        };
		avTransportService.setManager(avTransport);
		
		try {

            device = new LocalDevice(
                    new DeviceIdentity(UDN.uniqueSystemIdentifier("SALT ")),
                    new UDADeviceType("MediaRenderer", 1),
                    new DeviceDetails(
                            "DENON",
                            new ManufacturerDetails("Denon", "http://www.denon.com"),
                            new ModelDetails("*DRA-N4")
                    ),
                    new Icon[]{createDefaultDeviceIcon()},
                    new LocalService[]{
                            avTransportService,
                            renderingControlService,
                            connectionManagerService
                    }
            );

        } catch (ValidationException ex) {
            throw new RuntimeException(ex);
        }
		runLastChangePushThread();
	}

	private Icon createDefaultDeviceIcon() {
		// TODO Auto-generated method stub
		return null;
	}
	
    protected void runLastChangePushThread() {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        // These operations will NOT block and wait for network responses
                        avTransport.fireLastChange();
                        renderingControl.fireLastChange();
                        Thread.sleep(LAST_CHANGE_FIRING_INTERVAL_MILLISECONDS);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }.start();
    }
}
