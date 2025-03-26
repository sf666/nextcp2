package nextcp2.upnp.localdevice;

import java.io.File;
import java.net.URI;
import org.eclipse.jetty.util.StringUtil;
import org.jupnp.model.ModelUtil;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.support.avtransport.lastchange.AVTransportVariable;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.model.MediaInfo;
import org.jupnp.support.model.PositionInfo;
import org.jupnp.support.model.StorageMedium;
import org.jupnp.support.model.TransportAction;
import org.jupnp.support.model.TransportInfo;
import org.jupnp.support.model.TransportState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nextcp2Player implements IMediaPlayerCallback {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2Player.class.getName());

	final private UnsignedIntegerFourBytes instanceId;
	final private LastChange avTransportLastChange;
	final private LastChange renderingControlLastChange;

	private volatile TransportInfo currentTransportInfo = new TransportInfo();
	private PositionInfo currentPositionInfo = new PositionInfo();
	private MediaInfo currentMediaInfo = null;

	private IMediaPlayer mediaPlayerBackend = null;
	private Nextcp2Renderer rootDevice = null;

	private long trackNum = 0;
	
	private LocalDeviceConfig config = null;

	public Nextcp2Player(UnsignedIntegerFourBytes instanceId, LastChange avTransportLastChange, LastChange renderingControlLastChange,
		IMediaPlayerFactory mpf, Nextcp2Renderer rootDevice) {
		this.rootDevice = rootDevice;
		this.instanceId = instanceId;
		this.avTransportLastChange = avTransportLastChange;
		this.renderingControlLastChange = renderingControlLastChange;
		this.currentMediaInfo = createMediaInfo("", "", "", "", "00:00:00");
		this.currentTransportInfo = new TransportInfo(TransportState.STOPPED);
		if (mpf != null) {
			mediaPlayerBackend = mpf.createPlayer(this);
		}
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
		currentMediaInfo = createMediaInfo(currentMediaInfo.getCurrentURI(), currentMediaInfo.getCurrentURIMetaData(),
			currentMediaInfo.getNextURI(), currentMediaInfo.getNextURIMetaData(), newValue);

		getAvTransportLastChange().setEventedValue(getInstanceId(), new AVTransportVariable.CurrentTrackDuration(newValue),
			new AVTransportVariable.CurrentMediaDuration(newValue));
	}

	public void positionChanged(long seconds) {
		log.debug("position changed to " + seconds);
		currentPositionInfo = new PositionInfo(trackNum, currentMediaInfo.getMediaDuration(), currentMediaInfo.getCurrentURI(),
			ModelUtil.toTimeString(seconds), ModelUtil.toTimeString(seconds));
	}

	// Update LastChange
	synchronized protected void transportStateChanged(TransportState newState) {
		TransportState currentTransportState = currentTransportInfo.getCurrentTransportState();
		log.debug("transportStateChanged -> Current state is: " + currentTransportState + ", changing to new state: " + newState);
		if (!currentTransportState.equals(newState)) {
			currentTransportInfo = new TransportInfo(newState);

			getAvTransportLastChange().setEventedValue(getInstanceId(), new AVTransportVariable.TransportState(newState),
				new AVTransportVariable.CurrentTransportActions(getCurrentTransportActions()));
			rootDevice.fireLastChange();
		} else {
			log.debug("State didn't change.");
		}
	}

	// UPnP calls
	// =======================================================================================

	// requests

	synchronized public TransportInfo getCurrentTransportInfo() {
		log.debug("[getCurrentTransportInfo] current transport state : " + currentTransportInfo.getCurrentTransportState().getValue());
		return currentTransportInfo;
	}

	synchronized public PositionInfo getCurrentPositionInfo() {
		log.debug("[getCurrentPositionInfo] absolute time : " + currentPositionInfo.getAbsTime());
		return currentPositionInfo;
	}

	synchronized public MediaInfo getCurrentMediaInfo() {
		log.debug("[getCurrentMediaInfo] current URI : " + currentMediaInfo.getCurrentURI());
		return currentMediaInfo;
	}

	//
	public void setAVTransportURI(URI uri, String currentURIMetaData) {
		log.debug("setAVTransportURI : " + uri);
		currentMediaInfo = createMediaInfo(uri.toString(), currentURIMetaData, "", "", "00:00:00");
		currentPositionInfo = new PositionInfo(trackNum, currentURIMetaData, uri.toString());

		getAvTransportLastChange().setEventedValue(getInstanceId(), new AVTransportVariable.AVTransportURI(uri),
			new AVTransportVariable.CurrentTrackURI(uri));
		rootDevice.fireLastChange();
	}

	public void stop() {
		log.debug("stop");
		transportStateChanged(TransportState.STOPPED);
		mediaPlayerBackend.stop();
	}

	public void play(LocalDeviceConfig config) {
		log.debug("play");
		this.config = config;
		transportStateChanged(TransportState.PLAYING);
		if (mediaPlayerBackend != null) {
			mediaPlayerBackend.play(currentMediaInfo.getCurrentURI(), currentMediaInfo.getCurrentURIMetaData(), config);
		}
		rootDevice.fireLastChange();
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
		log.info("setNextAVTransportURI to : " + nextURI);
		currentMediaInfo = createMediaInfo(currentMediaInfo.getCurrentURI(), currentMediaInfo.getCurrentURIMetaData(), nextURI,
			nextURIMetaData, currentMediaInfo.getMediaDuration());
		rootDevice.fireLastChange();
	}

	public TransportAction[] getCurrentTransportActions() {
		TransportState state = currentTransportInfo.getCurrentTransportState();
		TransportAction[] actions;

		switch (state) {
			case STOPPED:
				actions = new TransportAction[] { TransportAction.Play };
				break;
			case PLAYING:
				actions = new TransportAction[] { TransportAction.Stop, TransportAction.Pause };
				break;
			case PAUSED_PLAYBACK:
				actions = new TransportAction[] { TransportAction.Stop, TransportAction.Pause, TransportAction.Play };
				break;
			default:
				actions = new TransportAction[] { TransportAction.Play };
		}
		if (log.isDebugEnabled()) {
			for (TransportAction transportAction : actions) {
				log.debug("current transport action : " + transportAction);
			}
		}
		return actions;
	}

	//
	// Callbacks from mediaPlayer backend
	//
	@Override
	public void skipToNextSong() {
		log.info("skip to next song ...");
//		transportStateChanged(TransportState.TRANSITIONING);
		if (StringUtil.isBlank(currentMediaInfo.getNextURI())) {
			log.info("no next title to skip to. Stopping ...");
			stop();
		} else {
			// trackNum++;
			log.info("moving to next uri ... ");
			setAVTransportURI(URI.create(currentMediaInfo.getNextURI()), currentMediaInfo.getNextURIMetaData());
			play(this.config);
		}
	}

	@Override
	public void stopped(File text) {
		rootDevice.sendPlayedSong(text);
	}

	private MediaInfo createMediaInfo(String curUri, String curMeta, String nextUri, String nextMeta, String duration) {
		return new MediaInfo(curUri, curMeta, nextUri, nextMeta, new UnsignedIntegerFourBytes(trackNum), duration, StorageMedium.NONE);
	}
}
