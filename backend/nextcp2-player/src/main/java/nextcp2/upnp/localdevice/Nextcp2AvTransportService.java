package nextcp2.upnp.localdevice;

import java.net.URI;
import java.util.logging.Logger;
import org.jupnp.http.HttpFetch;
import org.jupnp.model.types.ErrorCode;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.support.avtransport.AVTransportErrorCode;
import org.jupnp.support.avtransport.AVTransportException;
import org.jupnp.support.avtransport.impl.AVTransportService;
import org.jupnp.support.avtransport.impl.state.Stopped;
import org.jupnp.support.lastchange.LastChange;
import org.jupnp.support.model.DeviceCapabilities;
import org.jupnp.support.model.PlayMode;
import org.jupnp.support.model.StorageMedium;
import org.jupnp.support.model.TransportSettings;
import org.jupnp.util.URIUtil;

public class Nextcp2AvTransportService extends AVTransportService {

	public Nextcp2AvTransportService() {
		super(Nextcp2StateMachineDefinition.class, Stopped.class);
	}

	final private static Logger log = Logger.getLogger(Nextcp2AvTransportService.class.getName());

	@Override
	public void setAVTransportURI(UnsignedIntegerFourBytes instanceId, String currentURI, String currentURIMetaData)
		throws AVTransportException {
		URI uri;
		try {
			uri = new URI(currentURI);
		} catch (Exception ex) {
			throw new AVTransportException(ErrorCode.INVALID_ARGS, "CurrentURI can not be null or malformed");
		}

		if (currentURI.startsWith("http:")) {
			try {
				HttpFetch.validate(URIUtil.toURL(uri));
			} catch (Exception ex) {
				throw new AVTransportException(AVTransportErrorCode.RESOURCE_NOT_FOUND, ex.getMessage());
			}
		} else if (!currentURI.startsWith("file:")) {
			throw new AVTransportException(ErrorCode.INVALID_ARGS, "Only HTTP and file: resource identifiers are supported");
		}
	}

	@Override
	public DeviceCapabilities getDeviceCapabilities(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
		return new DeviceCapabilities(new StorageMedium[] { StorageMedium.NETWORK });
	}

	@Override
	public TransportSettings getTransportSettings(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
		return new TransportSettings(PlayMode.NORMAL);
	}

	@Override
	public void stop(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
	}

	@Override
	public void play(UnsignedIntegerFourBytes instanceId, String speed) throws AVTransportException {
	}

	@Override
	public void pause(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
	}

	@Override
	public void record(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
		// Not implemented
		log.info("### TODO: Not implemented: Record");
	}

	@Override
	public void seek(UnsignedIntegerFourBytes instanceId, String unit, String target) throws AVTransportException {
	}

	@Override
	public void next(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
		// Not implemented
		log.info("### TODO: Not implemented: Next");
	}

	@Override
	public void previous(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
		// Not implemented
		log.info("### TODO: Not implemented: Previous");
	}

	@Override
	public void setNextAVTransportURI(UnsignedIntegerFourBytes instanceId, String nextURI, String nextURIMetaData)
		throws AVTransportException {
		log.info("### TODO: Not implemented: SetNextAVTransportURI");
		// Not implemented
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
	public UnsignedIntegerFourBytes[] getCurrentInstanceIds() {
		UnsignedIntegerFourBytes[] ids = new UnsignedIntegerFourBytes[1];
		ids[0] = new UnsignedIntegerFourBytes(0);
		return ids;
	}
}
