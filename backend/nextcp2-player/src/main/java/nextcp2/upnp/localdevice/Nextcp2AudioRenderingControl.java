package nextcp2.upnp.localdevice;

import org.jupnp.internal.compat.java.beans.PropertyChangeSupport;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.model.types.UnsignedIntegerTwoBytes;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.model.Channel;
import org.jupnp.support.renderingcontrol.AbstractAudioRenderingControl;
import org.jupnp.support.renderingcontrol.RenderingControlException;


public class Nextcp2AudioRenderingControl extends AbstractAudioRenderingControl {

	public Nextcp2AudioRenderingControl() {
		// TODO Auto-generated constructor stub
	}

	public Nextcp2AudioRenderingControl(LastChange lastChange) {
		super(lastChange);
		// TODO Auto-generated constructor stub
	}

	public Nextcp2AudioRenderingControl(PropertyChangeSupport propertyChangeSupport) {
		super(propertyChangeSupport);
		// TODO Auto-generated constructor stub
	}

	public Nextcp2AudioRenderingControl(PropertyChangeSupport propertyChangeSupport, LastChange lastChange) {
		super(propertyChangeSupport, lastChange);
		// TODO Auto-generated constructor stub
	}

	@Override
	public UnsignedIntegerFourBytes[] getCurrentInstanceIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getMute(UnsignedIntegerFourBytes instanceId, String channelName) throws RenderingControlException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMute(UnsignedIntegerFourBytes instanceId, String channelName, boolean desiredMute) throws RenderingControlException {
		// TODO Auto-generated method stub

	}

	@Override
	public UnsignedIntegerTwoBytes getVolume(UnsignedIntegerFourBytes instanceId, String channelName) throws RenderingControlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVolume(UnsignedIntegerFourBytes instanceId, String channelName, UnsignedIntegerTwoBytes desiredVolume)
		throws RenderingControlException {
		// TODO Auto-generated method stub

	}

	@Override
	protected Channel[] getCurrentChannels() {
		// TODO Auto-generated method stub
		return null;
	}

}
