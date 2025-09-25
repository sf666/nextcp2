package nextcp2.upnp.localdevice;

import java.io.File;
import java.util.Map;
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
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.lastchange.LastChangeAwareServiceManager;
import org.jupnp.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nextcp2Renderer {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2Renderer.class.getName());
	
	public static final long LAST_CHANGE_FIRING_INTERVAL_MILLISECONDS = 500;

	final protected LocalServiceBinder binder = new AnnotationLocalServiceBinder();

	protected LastChange avTransportLastChange = null;
	protected LastChange renderingControlLastChange = null;

    protected static UDN staticUDN = new UDN("7889347a-746d-4ee5-b87f-401e1c8de34c");

	final protected ServiceManager<Nextcp2ConnectionManagerService> connectionManager;
	final protected LastChangeAwareServiceManager<Nextcp2AvTransportService> avTransport;
	final protected LastChangeAwareServiceManager<Nextcp2AudioRenderingControl> renderingControl;

	final protected Map<UnsignedIntegerFourBytes, Nextcp2Player> mediaPlayers;

	final protected LocalDevice device;
	private ISongPlayedCallback callback;

	private MediaPlayerConfigService configService = null;
	
	
	protected MediaPlayerConfigService getConfigService() {
		return configService;
	}

	public LocalDevice getLocalDevice() {
		return device;
	}

	public Nextcp2Renderer(IMediaPlayerFactory mpf, MediaPlayerConfigService configService, ISongPlayedCallback callback) {
		this.configService = configService;
		this.callback = callback;
		
		AVTransportLastChangeParser parser = new AVTransportLastChangeParser();
		avTransportLastChange = new LastChange(parser);
		RenderingControlLastChangeParser rcParser = new RenderingControlLastChangeParser();
		renderingControlLastChange = new LastChange(rcParser);

		// The media player instances
		mediaPlayers = new NextCp2MediaPlayers(1, avTransportLastChange, renderingControlLastChange, mpf, this);

		LocalService<Nextcp2AudioRenderingControl> renderingControlService = binder.read(Nextcp2AudioRenderingControl.class);
		renderingControl = new LastChangeAwareServiceManager<Nextcp2AudioRenderingControl>(renderingControlService,
			new RenderingControlLastChangeParser()) {

			@Override
			protected Nextcp2AudioRenderingControl createServiceInstance() throws Exception {
				return new Nextcp2AudioRenderingControl(renderingControlLastChange);
			}
		};
		renderingControlService.setManager(renderingControl);

		// The connection manager doesn't have to do much, HTTP is stateless
		LocalService connectionManagerService = binder.read(Nextcp2ConnectionManagerService.class);
		connectionManager = new DefaultServiceManager<Nextcp2ConnectionManagerService>(connectionManagerService,
			Nextcp2ConnectionManagerService.class) {

			@Override
			protected int getLockTimeoutMillis() {
				return 1000;
			}

			@Override
			protected Nextcp2ConnectionManagerService createServiceInstance() throws Exception {
				return new Nextcp2ConnectionManagerService();
			}
		};
		connectionManagerService.setManager(connectionManager);

		LocalService<Nextcp2AvTransportService> avTransportService = binder.read(Nextcp2AvTransportService.class);

		Nextcp2Renderer thisDevice = this;
		avTransport = new LastChangeAwareServiceManager<Nextcp2AvTransportService>(avTransportService, new AVTransportLastChangeParser()) {
			@Override
			protected Nextcp2AvTransportService createServiceInstance() throws Exception {
				return new Nextcp2AvTransportService(avTransportLastChange, mediaPlayers, configService, thisDevice);
			}
		};
		avTransportService.setManager(avTransport);

		try {

			device = new LocalDevice(
                new DeviceIdentity(staticUDN),
                new UDADeviceType("MediaRenderer", 1),
				new DeviceDetails("DENON",
                new ManufacturerDetails("Denon", "http://www.denon.com"),
                new ModelDetails("*DRA-N4")),
				new Icon[] { createDefaultDeviceIcon() },
				new LocalService[] { avTransportService, renderingControlService, connectionManagerService });

		} catch (ValidationException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void fireLastChange() {
		if (this.avTransport != null) {
			avTransport.fireLastChange();
		}
	}
	
	private Icon createDefaultDeviceIcon() {
		return null;
	}

	protected void sendPlayedSong(File file) {
		callback.songPlayed(file);
	}
}
