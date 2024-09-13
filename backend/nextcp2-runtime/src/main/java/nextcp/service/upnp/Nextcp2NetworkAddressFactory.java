package nextcp.service.upnp;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import org.apache.commons.lang.StringUtils;
import org.jupnp.transport.impl.NetworkAddressFactoryImpl;
import org.jupnp.transport.spi.InitializationException;
import org.jupnp.transport.spi.NoNetworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nextcp2NetworkAddressFactory extends NetworkAddressFactoryImpl {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2NetworkAddressFactory.class.getName());

	private String bindNetworkInterface = null;

	public Nextcp2NetworkAddressFactory(int streamListenPort, int multicastResponsePort) throws InitializationException {
		super(streamListenPort, multicastResponsePort);
	}

	public Nextcp2NetworkAddressFactory(int streamListenPort, int multicastResponsePort, String bindInterface)
		throws InitializationException {
		super(streamListenPort, multicastResponsePort);
		this.bindNetworkInterface = bindInterface;
		discoverNetworkInterfaces();
		discoverBindAddresses();
		if ((networkInterfaces.size() == 0 || bindAddresses.size() == 0)) {
			log.warn("No usable network interface or addresses found");
			if (requiresNetworkInterface()) {
				throw new NoNetworkException("Could not discover any usable network interfaces and/or addresses");
			}
		}
	}

	@Override
	protected void discoverNetworkInterfaces() throws InitializationException {
		bindAddresses.clear();
		networkInterfaces.clear();
		try {
			Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface iface : Collections.list(interfaceEnumeration)) {
				log.trace("Analyzing network interface: {}", iface.getDisplayName());
				if (isUsableNetworkInterface(iface)) {
					if (!StringUtils.isBlank(bindNetworkInterface)) {
						if (!iface.getDisplayName().equalsIgnoreCase(bindNetworkInterface)) {
							log.warn("ignore interface with name : " + iface.getDisplayName());
							continue;
						}
					}
					synchronized (networkInterfaces) {
						log.trace("Discovered usable network interface: {}", iface.getDisplayName());
						networkInterfaces.add(iface);
					}
				} else {
					log.trace("Ignoring non-usable network interface: {}", iface.getDisplayName());
				}
			}

		} catch (Exception ex) {
			throw new InitializationException("Could not not analyze local network interfaces: " + ex, ex);
		}
	}

}
