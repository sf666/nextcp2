package nextcp2.upnp.localdevice;

import java.net.URI;
import org.jupnp.model.ModelUtil;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.support.avtransport.lastchange.AVTransportVariable;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.model.MediaInfo;
import org.jupnp.support.model.PositionInfo;
import org.jupnp.support.model.TransportAction;
import org.jupnp.support.model.TransportInfo;
import org.jupnp.support.model.TransportState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp2.upnp.localdevice.LocalDeviceConfig.FileType;

public class Nextcp2Player implements IMediaPlayerCallback {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2Player.class.getName());

	final private UnsignedIntegerFourBytes instanceId;
	final private LastChange avTransportLastChange;
	final private LastChange renderingControlLastChange;

	private volatile TransportInfo currentTransportInfo = new TransportInfo();
	private PositionInfo currentPositionInfo = new PositionInfo();
	private MediaInfo currentMediaInfo = new MediaInfo();
	private MediaInfo nextMediaInfo = new MediaInfo();

	private IMediaPlayer mediaPlayerBackend = null;

	private LocalDeviceConfig testconfig = null;

	// TODO implement play(... );

	public Nextcp2Player(UnsignedIntegerFourBytes instanceId, LastChange avTransportLastChange, LastChange renderingControlLastChange,
		IMediaPlayerFactory mpf) {
		this.instanceId = instanceId;
		this.avTransportLastChange = avTransportLastChange;
		this.renderingControlLastChange = renderingControlLastChange;
		this.testconfig = getLocalDeviceTestConfig();
		if (mpf != null) {
			mediaPlayerBackend = mpf.createPlayer(this);
		}
	}

	private LocalDeviceConfig getLocalDeviceTestConfig() {
		LocalDeviceConfig c = new LocalDeviceConfig();
		c.fileType = FileType.ALBUM;
		c.overwrite = false;
		c.workdir = "/tmp";
		c.script = "";
		return c;
	}

	public UnsignedIntegerFourBytes getInstanceId() {
		return instanceId;
	}

	public LastChange getAvTransportLastChange() {
		return avTransportLastChange;
	}

	public LastChange getRenderingControlLastChange() {
		return renderingControlLastChange;
	}

	// control position & duration
	public void durationChanged(long seconds) {
		log.debug("Duration Changed event received: " + seconds);
		String newValue = ModelUtil.toTimeString(seconds);
		currentMediaInfo = new MediaInfo(currentMediaInfo.getCurrentURI(), "", new UnsignedIntegerFourBytes(1), newValue,
			org.jupnp.support.model.StorageMedium.NETWORK);

		getAvTransportLastChange().setEventedValue(getInstanceId(), new AVTransportVariable.CurrentTrackDuration(newValue),
			new AVTransportVariable.CurrentMediaDuration(newValue));
	}

	public void positionChanged(long seconds) {
		log.debug("position changed to " + seconds);
		currentPositionInfo = new PositionInfo(1, currentMediaInfo.getMediaDuration(), currentMediaInfo.getCurrentURI(),
			ModelUtil.toTimeString(seconds), ModelUtil.toTimeString(seconds));
	}

	// Update LastChange
	synchronized protected void transportStateChanged(TransportState newState) {
		TransportState currentTransportState = currentTransportInfo.getCurrentTransportState();
		log.debug("transportStateChanged -> Current state is: " + currentTransportState + ", changing to new state: " + newState);
		currentTransportInfo = new TransportInfo(newState);

		getAvTransportLastChange().setEventedValue(getInstanceId(), new AVTransportVariable.TransportState(newState),
			new AVTransportVariable.CurrentTransportActions(getCurrentTransportActions()));
	}

	// UPnP calls
	// =======================================================================================

	// requests

	synchronized public TransportInfo getCurrentTransportInfo() {
		log.debug("getCurrentTransportInfo " + currentTransportInfo);
		return currentTransportInfo;
	}

	synchronized public PositionInfo getCurrentPositionInfo() {
		log.debug("getCurrentPositionInfo " + currentPositionInfo);
		return currentPositionInfo;
	}

	synchronized public MediaInfo getCurrentMediaInfo() {
		log.debug("getCurrentMediaInfo " + currentMediaInfo);
		return currentMediaInfo;
	}

	//
	public void setAVTransportURI(URI uri, String currentURIMetaData) {
		log.debug("set uri : " + uri);
		stop();
		currentMediaInfo = new MediaInfo(uri.toString(), currentURIMetaData);
		currentPositionInfo = new PositionInfo(1, "", uri.toString());

		getAvTransportLastChange().setEventedValue(getInstanceId(), new AVTransportVariable.AVTransportURI(uri),
			new AVTransportVariable.CurrentTrackURI(uri));

		transportStateChanged(TransportState.STOPPED);
	}

	public void stop() {
		log.debug("stop");
		transportStateChanged(TransportState.STOPPED);
	}

	public void play() {
		log.debug("play");
		transportStateChanged(TransportState.PLAYING);
		if (mediaPlayerBackend != null) {
			mediaPlayerBackend.play(currentMediaInfo.getCurrentURI(), currentMediaInfo.getCurrentURI(), testconfig);
		}
	}

	public void pause() {
		log.debug("pause");
		transportStateChanged(TransportState.PAUSED_PLAYBACK);
	}

	public void seek(String unit, String target) {
		log.warn("seek not implemented. ");
	}

	public void next() {
		log.debug("next");
	}

	public void previous() {
		log.debug("previous");
	}

	public void setNextAVTransportURI(String nextURI, String nextURIMetaData) {
		nextMediaInfo = new MediaInfo(nextURI, nextURIMetaData);
	}

	public TransportAction[] getCurrentTransportActions() {
		TransportState state = currentTransportInfo.getCurrentTransportState();
		TransportAction[] actions;

		switch (state) {
			case STOPPED:
				actions = new TransportAction[] { TransportAction.Play };
				break;
			case PLAYING:
				actions = new TransportAction[] { TransportAction.Stop, TransportAction.Pause, TransportAction.Seek };
				break;
			case PAUSED_PLAYBACK:
				actions = new TransportAction[] { TransportAction.Stop, TransportAction.Pause, TransportAction.Seek, TransportAction.Play };
				break;
			default:
				actions = null;
		}
		return actions;
	}

	//
	// Callbacks from mediaPlayer backend
	//

	@Override
	public void skipToNextSong() {
		transportStateChanged(TransportState.TRANSITIONING);
		log.debug("skip to next song ...");
		currentMediaInfo = nextMediaInfo;
		play();
	}

	@Override
	public void finished(String text) {
		log.debug("finished.");
		stop();
	}
}
