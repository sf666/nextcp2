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

import org.jupnp.support.connectionmanager.ConnectionManagerService;
import org.jupnp.support.model.Protocol;
import org.jupnp.support.model.ProtocolInfo;
import org.jupnp.support.model.ProtocolInfos;
import org.jupnp.util.MimeType;

public class Nextcp2ConnectionManagerService extends ConnectionManagerService {

	public Nextcp2ConnectionManagerService() {
		super(getSourceProtocolInfos(), getSinkProtocolInfos());
	}

	private static ProtocolInfos getSinkProtocolInfos() {
		return new ProtocolInfos(
			new ProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, "audio/*", ProtocolInfo.WILDCARD),
			new ProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, "audio/wav", "DLNA.ORG_PN=WAV;DLNA.ORG_OP=01")
			);
	}

	private static ProtocolInfos getSourceProtocolInfos() {
		return new ProtocolInfos(
			new ProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, MimeType.WILDCARD, ProtocolInfo.WILDCARD));
	}

}
