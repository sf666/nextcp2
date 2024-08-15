package nextcp.devicedriver;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import nextcp.dto.Config;

@Component
public class DeviceDriverDiscoveryService {

	private static final Logger log = LoggerFactory.getLogger(DeviceDriverDiscoveryService.class.getName());

	@Autowired
	Config config = null;

	private HashMap<String, IDeviceDriverFactory> availableDeviceDriver = new HashMap<>();

	public Set<DeviceCapabilityDto> getAvailableDeviceDriverTypes() {
		Set<DeviceCapabilityDto> capabilities = new HashSet<>();
		for (IDeviceDriverFactory driver : availableDeviceDriver.values()) {
			capabilities.add(new DeviceCapabilityDto(driver));
		}
		return capabilities;
	}

	@PostConstruct
	public void init() {
		try {
			log.info("searching for device driver in directory : " + config.applicationConfig.libraryPath);
			File loc = new File(config.applicationConfig.libraryPath);

			File[] flist = loc.listFiles(new FileFilter() {

				public boolean accept(File file) {
					return !file.getName().toLowerCase().startsWith("mp-") && file.getName().toLowerCase().endsWith(".jar");
				}
			});

			loadDeviceDriver(flist);
		} catch (Exception e) {
			log.error("ServiceLoader Error", e);
		}
	}

	private void loadDeviceDriver(File[] flist) throws MalformedURLException {
		if (flist != null) {
			List<URL> urls = new ArrayList<>();

			for (int i = 0; i < flist.length; i++) {
				if (!flist[i].getName().startsWith("mp-")) {
					urls.add(flist[i].toURI().toURL());
				}
			}

			URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[0]), this.getClass().getClassLoader());

			ServiceLoader<IDeviceDriverFactory> loader = ServiceLoader.load(IDeviceDriverFactory.class, ucl);
			for (IDeviceDriverFactory factory : loader) {
				availableDeviceDriver.put(factory.getDriverCapabilities().getDeviceType(), factory);
				log.info(String.format("Found Driver for device of type : %s ", factory.getDriverCapabilities().getDeviceType()));
			}
		} else {
			log.debug("no device driver available.");
		}
	}

	public boolean deviceDriverExists(String type) {
		if (StringUtils.isBlank(type) || availableDeviceDriver.get(type) == null) {
			log.warn("device driver type not available : " + type);
			return false;
		}
		return true;
	}

	public IDeviceDriverService getDeviceDriver(String type, String connectionString, IDeviceDriverCallback callback, String rendererUdn) {
		try {
			if (StringUtils.isBlank(connectionString) || StringUtils.isBlank(type)) {
				log.warn("device driver needs type and connection string");
			}
			if (availableDeviceDriver.get(type) != null) {
				return availableDeviceDriver.get(type).getDriverFor(connectionString, callback, rendererUdn);
			} else {
				log.warn("device driver of type " + type + " not registered.");
			}
			return null;
		} catch (Exception e) {
			log.error("getDeviceDriver", e);
		}
		return null;
	}
}
