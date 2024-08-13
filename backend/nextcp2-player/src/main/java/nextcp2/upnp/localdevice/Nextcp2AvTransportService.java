package nextcp2.upnp.localdevice;

import java.net.URI;
import java.util.Map;
import org.jupnp.http.HttpFetch;
import org.jupnp.model.types.ErrorCode;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.support.avtransport.AVTransportErrorCode;
import org.jupnp.support.avtransport.AVTransportException;
import org.jupnp.support.avtransport.AbstractAVTransportService;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.model.DeviceCapabilities;
import org.jupnp.support.model.MediaInfo;
import org.jupnp.support.model.PlayMode;
import org.jupnp.support.model.PositionInfo;
import org.jupnp.support.model.StorageMedium;
import org.jupnp.support.model.TransportAction;
import org.jupnp.support.model.TransportInfo;
import org.jupnp.support.model.TransportSettings;
import org.jupnp.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nextcp2AvTransportService extends AbstractAVTransportService {
	
	private static final Logger log = LoggerFactory.getLogger(Nextcp2AvTransportService.class.getName());
	
    final private Map<UnsignedIntegerFourBytes, Nextcp2Player> players;

    protected Nextcp2AvTransportService(LastChange lastChange, Map<UnsignedIntegerFourBytes, Nextcp2Player> players) {
        super(lastChange);
        this.players = players;
    }

    protected Map<UnsignedIntegerFourBytes, Nextcp2Player> getPlayers() {
        return players;
    }

    protected Nextcp2Player getInstance(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
    	Nextcp2Player player = getPlayers().get(instanceId);
        if (player == null) {
            throw new AVTransportException(AVTransportErrorCode.INVALID_INSTANCE_ID);
        }
        return player;
    }

    @Override
    public void setAVTransportURI(UnsignedIntegerFourBytes instanceId,
                                  String currentURI,
                                  String currentURIMetaData) throws AVTransportException {
        URI uri;
        try {
            uri = new URI(currentURI);
        } catch (Exception ex) {
            throw new AVTransportException(
                    ErrorCode.INVALID_ARGS, "CurrentURI can not be null or malformed"
            );
        }

        if (currentURI.startsWith("http:")) {
            try {
                HttpFetch.validate(URIUtil.toURL(uri));
            } catch (Exception ex) {
                throw new AVTransportException(
                        AVTransportErrorCode.RESOURCE_NOT_FOUND, ex.getMessage()
                );
            }
        } else if (!currentURI.startsWith("file:")) {
            throw new AVTransportException(
                    ErrorCode.INVALID_ARGS, "Only HTTP and file: resource identifiers are supported"
            );
        }

        // TODO: Check mime type of resource against supported types

        // TODO: DIDL fragment parsing and handling of currentURIMetaData

        getInstance(instanceId).setURI(uri);
    }

    @Override
    public MediaInfo getMediaInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return getInstance(instanceId).getCurrentMediaInfo();
    }

    @Override
    public TransportInfo getTransportInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return getInstance(instanceId).getCurrentTransportInfo();
    }

    @Override
    public PositionInfo getPositionInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return getInstance(instanceId).getCurrentPositionInfo();
    }

    @Override
    public DeviceCapabilities getDeviceCapabilities(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        getInstance(instanceId);
        return new DeviceCapabilities(new StorageMedium[]{StorageMedium.NETWORK});
    }

    @Override
    public TransportSettings getTransportSettings(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        getInstance(instanceId);
        return new TransportSettings(PlayMode.NORMAL);
    }

    @Override
    public void stop(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        getInstance(instanceId).stop();
    }

    @Override
    public void play(UnsignedIntegerFourBytes instanceId, String speed) throws AVTransportException {
        getInstance(instanceId).play();
    }

    @Override
    public void pause(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        getInstance(instanceId).pause();
    }

    @Override
    public void record(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        // Not implemented
        log.info("### TODO: Not implemented: Record");
    }

    @Override
    public void seek(UnsignedIntegerFourBytes instanceId, String unit, String target) throws AVTransportException {
        final Nextcp2Player player = getInstance(instanceId);
        player.seek(unit, target);
    }

    @Override
    public void next(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        getInstance(instanceId).next();
    }

    @Override
    public void previous(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        getInstance(instanceId).previous();
    }

    @Override
    public void setNextAVTransportURI(UnsignedIntegerFourBytes instanceId,
                                      String nextURI,
                                      String nextURIMetaData) throws AVTransportException {
        getInstance(instanceId).setNextAVTransportURI(nextURI, nextURIMetaData);
    }

    @Override
    public void setPlayMode(UnsignedIntegerFourBytes instanceId, String newPlayMode) throws AVTransportException {
        // Not implemented
        log.info("### TODO: Not implemented: SetPlayMode");
    }

    @Override
    public void setRecordQualityMode(UnsignedIntegerFourBytes instanceId, String newRecordQualityMode) throws AVTransportException {
        // Not implemented
        log.info("### TODO: Not implemented: SetRecordQualityMode");
    }

    @Override
    protected TransportAction[] getCurrentTransportActions(UnsignedIntegerFourBytes instanceId) throws Exception {
        return getInstance(instanceId).getCurrentTransportActions();
    }

    @Override
    public UnsignedIntegerFourBytes[] getCurrentInstanceIds() {
        UnsignedIntegerFourBytes[] ids = new UnsignedIntegerFourBytes[getPlayers().size()];
        int i = 0;
        for (UnsignedIntegerFourBytes id : getPlayers().keySet()) {
            ids[i] = id;
            i++;
        }
        return ids;
    }
}
