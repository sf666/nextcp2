package nextcp.upnp.device.mediaserver.cds;

import java.util.ArrayList;
import java.util.List;
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

	public ContentDirectoryEventListener(RemoteDevice device, MediaServerDevice mediaServerDevice) {
		super(device);
		this.mediaServerDevice = mediaServerDevice;
	}

	@Override
	public void eventReceived(String key, StateVariableValue<RemoteService> stateVar) {
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
