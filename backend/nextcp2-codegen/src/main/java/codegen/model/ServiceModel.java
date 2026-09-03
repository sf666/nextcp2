package codegen.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Everything the generator knows about one UPnP service type, accumulated over every device that
 * ever announced it.
 *
 * Two devices offering the same service type write to the same generated package, and they rarely
 * offer the same set of actions: the standard marks most of them optional and vendors add their
 * own. Generating from whichever device was seen last therefore removed capabilities again. The
 * model is the union instead, and the generated code is a pure function of it.
 */
public class ServiceModel {

	private final String namespace;
	private final String serviceType;
	private final int version;
	private boolean events = false;

	private final Map<String, VariableModel> stateVariables = new TreeMap<>();
	private final Map<String, ActionModel> actions = new TreeMap<>();

	public ServiceModel(String namespace, String serviceType, int version) {
		this.namespace = namespace;
		this.serviceType = serviceType;
		this.version = version;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getServiceType() {
		return serviceType;
	}

	public int getVersion() {
		return version;
	}

	public boolean hasEvents() {
		return events;
	}

	public void setEvents(boolean events) {
		this.events = events;
	}

	public Map<String, VariableModel> getStateVariables() {
		return stateVariables;
	}

	public Map<String, ActionModel> getActions() {
		return actions;
	}

	public List<VariableModel> getStateVariableList() {
		return List.copyOf(stateVariables.values());
	}

	public void addStateVariable(VariableModel variable) {
		VariableModel known = stateVariables.get(variable.getName());
		stateVariables.put(variable.getName(), known == null ? variable : known.mergedWith(variable));
	}

	public ActionModel action(String name) {
		return actions.computeIfAbsent(name, ActionModel::new);
	}

	/**
	 * Adds everything the other model knows and reports the java types that disagree. Nothing is
	 * removed and no type is overwritten - a conflict is reported and the stored type kept, because
	 * the generated code converts incoming values rather than casting them (see nextcp.upnp.UpnpValue).
	 *
	 * @return true when this model gained something.
	 */
	public MergeResult mergeFrom(ServiceModel other) {
		List<String> conflicts = new ArrayList<>();
		int before = fingerprint();

		if (other.events) {
			events = true;
		}
		for (VariableModel variable : other.stateVariables.values()) {
			VariableModel known = stateVariables.get(variable.getName());
			if (known != null && !known.getJavaType().equals(variable.getJavaType())) {
				conflicts.add(String.format("state variable %s: %s <> %s", variable.getName(), known.getJavaType(),
					variable.getJavaType()));
			}
			addStateVariable(variable);
		}
		for (ActionModel other_action : other.actions.values()) {
			conflicts.addAll(action(other_action.getName()).mergeFrom(other_action));
		}
		return new MergeResult(fingerprint() != before, conflicts);
	}

	private int fingerprint() {
		int result = Boolean.hashCode(events) + stateVariables.keySet().hashCode();
		for (ActionModel action : actions.values()) {
			result = 31 * result + action.getName().hashCode() + action.getInput().keySet().hashCode()
				+ action.getOutput().keySet().hashCode();
		}
		return result;
	}

	public record MergeResult(boolean changed, List<String> conflicts) {
	}
}
