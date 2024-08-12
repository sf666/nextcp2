package nextcp.service.upnp;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import org.jupnp.transport.impl.NetworkAddressFactoryImpl;
import org.jupnp.transport.spi.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nextcp2NetworkAddressFactory extends NetworkAddressFactoryImpl {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2NetworkAddressFactory.class.getName());

	private static boolean overrideNetworkInterface = false;
	private static String bindNetworkInterface = "en0";
	
	public Nextcp2NetworkAddressFactory(int streamListenPort, int multicastResponsePort) throws InitializationException {
		super(streamListenPort, multicastResponsePort);
	}

	@Override
    protected void discoverNetworkInterfaces() throws InitializationException {
        try {

            Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface iface : Collections.list(interfaceEnumeration)) {
                log.trace("Analyzing network interface: {}", iface.getDisplayName());
                if (isUsableNetworkInterface(iface)) {
                	if (overrideNetworkInterface) {
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
