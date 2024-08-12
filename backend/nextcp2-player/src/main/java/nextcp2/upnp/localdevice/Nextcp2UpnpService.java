/*
 * This file is part of Universal Media Server, based on PS3 Media Server.
 *
 * This program is a free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; version 2 of the License only.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package nextcp2.upnp.localdevice;

import org.jupnp.DefaultUpnpServiceConfiguration;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.protocol.ProtocolFactory;
import org.jupnp.registry.DefaultRegistryListener;
import org.jupnp.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTPMU/HTTPU only implementation. Use it's own ssdp upnp server if
 * MediaServerDevice is registered. Need SocketSSDPServer to start if no device
 * or service are registered. Need external http upnp server as no service are
 * implemented.
 */
public class Nextcp2UpnpService extends UpnpServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(Nextcp2UpnpService.class);

	public Nextcp2UpnpService() {
		super(new DefaultUpnpServiceConfiguration());
	}

	@Override
	protected Registry createRegistry(ProtocolFactory protocolFactory) {
		Registry result = super.createRegistry(protocolFactory);
		result.addListener(new DefaultRegistryListener());
		return result;
	}

}
