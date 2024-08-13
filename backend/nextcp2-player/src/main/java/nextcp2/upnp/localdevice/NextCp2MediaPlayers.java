/*
 * Copyright (C) 2013 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU Lesser
 * General Public License Version 2 or later ("LGPL") or the Common Development
 * and Distribution License Version 1 or later ("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the License.
 * See LICENSE.txt for more information.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 */

package nextcp2.upnp.localdevice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.model.TransportState;

public class NextCp2MediaPlayers extends ConcurrentHashMap<UnsignedIntegerFourBytes, Nextcp2Player> {

	private static final long serialVersionUID = 1L;

	final private static Logger log = Logger.getLogger(NextCp2MediaPlayers.class.getName());

	final protected LastChange avTransportLastChange;
	final protected LastChange renderingControlLastChange;

	public NextCp2MediaPlayers(int numberOfPlayers, LastChange avTransportLastChange, LastChange renderingControlLastChange) {
		super(numberOfPlayers);
		this.avTransportLastChange = avTransportLastChange;
		this.renderingControlLastChange = renderingControlLastChange;

		for (int i = 0; i < numberOfPlayers; i++) {

			Nextcp2Player player = new Nextcp2Player(new UnsignedIntegerFourBytes(i), avTransportLastChange, renderingControlLastChange) {
			};
			put(player.getInstanceId(), player);
		}
	}
}
