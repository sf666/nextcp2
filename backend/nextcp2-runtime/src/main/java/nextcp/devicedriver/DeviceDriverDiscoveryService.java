package nextcp.devicedriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import nextcp.dto.Config;
import nextcp.util.BackendException;

@Component
public class DeviceDriverDiscoveryService {

	private static final Logger log = LoggerFactory.getLogger(DeviceDriverDiscoveryService.class);

	private final Config config;

	// Registered factories (loaded at startup)
	private final Map<String, IDeviceDriverFactory> availableDeviceDriver = new ConcurrentHashMap<>();

	// Active driver instances for lifecycle management
	private final Map<String, IDeviceDriverService> activeDrivers = new ConcurrentHashMap<>();

	@Autowired
	public DeviceDriverDiscoveryService(Config config) {
		this.config = config;
	}

	@PostConstruct
	public void init() {
		String libraryPath = config.applicationConfig.libraryPath;
		log.info("Searching for device drivers in directory: {}", libraryPath);

		File loc = new File(libraryPath);
		if (!loc.exists()) {
			log.warn("Library path does not exist: {}", loc.getAbsolutePath());
			return;
		}
		if (!loc.isDirectory()) {
			log.error("Library path is not a directory: {}", loc.getAbsolutePath());
			return;
		}
		if (!loc.canRead()) {
			log.error("Library path is not readable: {}", loc.getAbsolutePath());
			return;
		}

		File[] flist = loc.listFiles(file -> {
			String name = file.getName().toLowerCase();
			return !name.startsWith("mp-") && name.endsWith(".jar");
		});

		try {
			loadDeviceDriver(flist);
		} catch (Exception e) {
			log.error("Error loading device drivers", e);
		}
	}

	@PreDestroy
	public void shutdown() {
		log.info("Shutting down {} active device driver(s) ...", activeDrivers.size());
		activeDrivers.forEach((udn, driver) -> {
			try {
				log.info("Stopping device driver for renderer: {}", udn);
				driver.stop();
			} catch (Exception e) {
				log.error("Error stopping device driver for renderer: {}", udn, e);
			}
		});
		activeDrivers.clear();
	}

	public Set<DeviceCapabilityDto> getAvailableDeviceDriverTypes() {
		Set<DeviceCapabilityDto> capabilities = new HashSet<>();
		for (IDeviceDriverFactory driver : availableDeviceDriver.values()) {
			capabilities.add(new DeviceCapabilityDto(driver));
		}
		return capabilities;
	}

	public boolean deviceDriverExists(String type) {
		if (StringUtils.isBlank(type) || !availableDeviceDriver.containsKey(type)) {
			log.warn("Device driver type not available: '{}'", type);
			return false;
		}
		return true;
	}

	public IDeviceDriverService getDeviceDriver(String type, String connectionString, IDeviceDriverCallback callback, String rendererUdn) {
		if (StringUtils.isBlank(type) || StringUtils.isBlank(connectionString)) {
			throw new IllegalArgumentException("type and connectionString must not be blank");
		}

		IDeviceDriverFactory factory = availableDeviceDriver.get(type);
		if (factory == null) {
			log.warn("Device driver of type '{}' not registered", type);
			throw new BackendException(BackendException.DEVICE_DRIVER_UNAVAILABLE,
				String.format("No device driver registered for type '%s'", type));
		}

		IDeviceDriverService driver = factory.getDriverFor(connectionString, callback, rendererUdn);
		if (driver != null) {
			activeDrivers.put(rendererUdn, driver);
			log.info("Device driver for type '{}' registered for renderer: {}", type, rendererUdn);
		}
		return driver;
	}

	private void loadDeviceDriver(File[] flist) throws MalformedURLException {
		if (flist == null || flist.length == 0) {
			log.debug("No device driver JARs found");
			return;
		}

		List<URL> urls = new ArrayList<>();
		for (File file : flist) {
			urls.add(file.toURI().toURL());
			log.debug("Adding JAR to classloader: {}", file.getName());
		}

		URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[0]), this.getClass().getClassLoader());

		ServiceLoader<IDeviceDriverFactory> loader = ServiceLoader.load(IDeviceDriverFactory.class, ucl);

		for (IDeviceDriverFactory factory : loader) {
			String deviceType = factory.getDriverCapabilities().getDeviceType();
			availableDeviceDriver.put(deviceType, factory);
			log.info("Found driver for device type: '{}'", deviceType);
		}
	}
}