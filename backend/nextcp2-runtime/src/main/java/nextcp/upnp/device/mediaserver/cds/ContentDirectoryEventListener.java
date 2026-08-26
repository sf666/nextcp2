package nextcp.upnp.device.mediaserver.cds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.state.StateVariableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.ContentDirectoryServiceEventListenerImpl;

/**
 * Listens for ContainerUpdateIDs on a media server's ContentDirectory service.
 *
 * A server answers a browse with what it has and reports late arrivals through this event - a web
 * playlist whose streams were still resolving, for example. The generated listener knows nothing
 * about the variable (it is optional and was not in the description the model was generated from),
 * but the generic callback below receives every state variable the event carries.
 */
public class ContentDirectoryEventListener extends ContentDirectoryServiceEventListenerImpl {

	private static final Logger log = LoggerFactory.getLogger(ContentDirectoryEventListener.class.getName());
	private static final String CONTAINER_UPDATE_IDS = "ContainerUpdateIDs";

	private final MediaServerDevice mediaServerDevice;

	/**
	 * Whether the GENA subscription is currently carrying events. False until the server confirms it, and
	 * false again once it fails or ends, so a watchdog can subscribe again - nothing else retried, and a
	 * subscription that never came up stayed down until the media server itself restarted.
	 */
	private final AtomicBoolean subscribed = new AtomicBoolean(false);

	public ContentDirectoryEventListener(RemoteDevice device, MediaServerDevice mediaServerDevice) {
		super(device);
		this.mediaServerDevice = mediaServerDevice;
	}

	public boolean isSubscribed() {
		return subscribed.get();
	}

	@Override
	public void established() {
		super.established();
		subscribed.set(true);
		log.info("content directory subscription established for {}", mediaServerDevice.getFriendlyName());
	}

	@Override
	public void failed(UpnpResponse responseStatus) {
		super.failed(responseStatus);
		subscribed.set(false);
		log.warn("content directory subscription failed for {} : {}", mediaServerDevice.getFriendlyName(), responseStatus);
	}

	@Override
	public void ended(CancelReason reason, UpnpResponse responseStatus) {
		super.ended(reason, responseStatus);
		subscribed.set(false);
		log.warn("content directory subscription ended for {} : reason {}, response {}",
			mediaServerDevice.getFriendlyName(), reason, responseStatus);
	}

	@Override
	public void eventReceived(String key, StateVariableValue<RemoteService> stateVar) {
		// An event is proof that the subscription carries, whatever the callbacks reported.
		subscribed.set(true);
		if (!CONTAINER_UPDATE_IDS.equals(key)) {
			return;
		}
		List<String> containerIds = parseContainerIds(stateVar.getValue());
		if (containerIds.isEmpty()) {
			return;
		}
		log.debug("container content changed : {}", containerIds);
		mediaServerDevice.containerContentChanged(containerIds);
	}

	/**
	 * The event value is a CSV of containerID,containerUpdateID pairs. Only the ids are of interest,
	 * the update id is what the server compares against, not us.
	 */
	private static List<String> parseContainerIds(Object value) {
		if (value == null) {
			return List.of();
		}
		String[] parts = value.toString().split(",");
		List<String> containerIds = new ArrayList<>();
		for (int i = 0; i < parts.length; i += 2) {
			String containerId = parts[i].trim();
			if (!containerId.isEmpty()) {
				containerIds.add(containerId);
			}
		}
		return containerIds;
	}
}
