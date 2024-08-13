package nextcp2.upnp.localdevice;

import org.jupnp.internal.compat.java.beans.PropertyChangeSupport;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.model.types.UnsignedIntegerTwoBytes;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.model.Channel;
import org.jupnp.support.renderingcontrol.AbstractAudioRenderingControl;
import org.jupnp.support.renderingcontrol.RenderingControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nextcp2AudioRenderingControl extends AbstractAudioRenderingControl {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2AudioRenderingControl.class.getName());

	public Nextcp2AudioRenderingControl() {
		log.debug("constructor");
	}

	public Nextcp2AudioRenderingControl(LastChange lastChange) {
		super(lastChange);
		log.debug("constructor with lastChange");
	}

	public Nextcp2AudioRenderingControl(PropertyChangeSupport propertyChangeSupport) {
		super(propertyChangeSupport);
		log.debug("constructor with propertyChangeSupport");
	}

	public Nextcp2AudioRenderingControl(PropertyChangeSupport propertyChangeSupport, LastChange lastChange) {
		super(propertyChangeSupport, lastChange);
		log.debug("constructor with lastChange & propertyChangeSupport");
	}

	@Override
	public UnsignedIntegerFourBytes[] getCurrentInstanceIds() {
		return new UnsignedIntegerFourBytes[] {new UnsignedIntegerFourBytes(0)};
	}

	@Override
	public boolean getMute(UnsignedIntegerFourBytes instanceId, String channelName) throws RenderingControlException {
		log.debug("get mute");
		return false;
	}

	@Override
	public void setMute(UnsignedIntegerFourBytes instanceId, String channelName, boolean desiredMute) throws RenderingControlException {
		log.debug("set mute");
	}

	@Override
	public UnsignedIntegerTwoBytes getVolume(UnsignedIntegerFourBytes instanceId, String channelName) throws RenderingControlException {
		log.debug("get vol");
		return new UnsignedIntegerTwoBytes(0);
	}

	@Override
	public void setVolume(UnsignedIntegerFourBytes instanceId, String channelName, UnsignedIntegerTwoBytes desiredVolume)
		throws RenderingControlException {
		log.debug("set vol");
	}

	@Override
	protected Channel[] getCurrentChannels() {
		log.debug("getCurrentChannels");
		return new Channel[0];
	}

}
