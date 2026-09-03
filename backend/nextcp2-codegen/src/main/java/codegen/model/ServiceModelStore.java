package codegen.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * Reads and writes the accumulated service model as YAML, one file per generated package.
 *
 * The file sits next to the classes it produced so it travels with them - the generator on a
 * long-running installation writes both into the same tree, and a harvest picks up both. Keys are
 * held in sorted maps, so a rewrite only shows up in a diff when the model actually gained
 * something.
 */
public class ServiceModelStore {

	public static final String FILE_NAME = "service-model.yaml";

	private static final Logger log = LoggerFactory.getLogger(ServiceModelStore.class.getName());

	private final Yaml yaml;

	public ServiceModelStore() {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		options.setIndent(2);
		yaml = new Yaml(options);
	}

	public ServiceModel read(File directory) {
		File file = new File(directory, FILE_NAME);
		if (!file.isFile()) {
			return null;
		}
		try (Reader reader = new FileReader(file)) {
			Map<String, Object> root = yaml.load(reader);
			return root == null ? null : fromMap(root);
		} catch (Exception e) {
			log.error("cannot read service model {}", file.getAbsolutePath(), e);
			return null;
		}
	}

	public void write(File directory, ServiceModel model) {
		File file = new File(directory, FILE_NAME);
		if (!directory.isDirectory() && !directory.mkdirs()) {
			log.error("cannot create {}", directory.getAbsolutePath());
			return;
		}
		// Written aside and moved into place, so a reader never sees a half written file.
		File temporary = new File(directory, FILE_NAME + ".tmp");
		try {
			try (Writer writer = new FileWriter(temporary)) {
				yaml.dump(toMap(model), writer);
			}
			Files.move(temporary.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			log.error("cannot write service model {}", file.getAbsolutePath(), e);
			temporary.delete();
		}
	}

	private Map<String, Object> toMap(ServiceModel model) {
		Map<String, Object> root = new LinkedHashMap<>();
		root.put("namespace", model.getNamespace());
		root.put("serviceType", model.getServiceType());
		root.put("version", model.getVersion());
		root.put("events", model.hasEvents());

		Map<String, Object> variables = new LinkedHashMap<>();
		model.getStateVariables().forEach((name, variable) -> variables.put(name, toMap(variable)));
		root.put("stateVariables", variables);

		Map<String, Object> actions = new LinkedHashMap<>();
		model.getActions().forEach((name, action) -> {
			Map<String, Object> entry = new LinkedHashMap<>();
			entry.put("input", toMap(action.getInput()));
			entry.put("output", toMap(action.getOutput()));
			actions.put(name, entry);
		});
		root.put("actions", actions);
		return root;
	}

	private Map<String, Object> toMap(Map<String, VariableModel> variables) {
		Map<String, Object> result = new LinkedHashMap<>();
		variables.forEach((name, variable) -> result.put(name, toMap(variable)));
		return result;
	}

	private Map<String, Object> toMap(VariableModel variable) {
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("java", variable.getJavaType());
		if (variable.getUpnpType() != null) {
			result.put("upnp", variable.getUpnpType());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private ServiceModel fromMap(Map<String, Object> root) {
		ServiceModel model = new ServiceModel((String) root.get("namespace"), (String) root.get("serviceType"),
			((Number) root.get("version")).intValue());
		model.setEvents(Boolean.TRUE.equals(root.get("events")));

		Map<String, Object> variables = (Map<String, Object>) root.get("stateVariables");
		if (variables != null) {
			variables.forEach((name, value) -> model.addStateVariable(variable(name, (Map<String, Object>) value)));
		}

		Map<String, Object> actions = (Map<String, Object>) root.get("actions");
		if (actions != null) {
			actions.forEach((name, value) -> {
				ActionModel action = model.action(name);
				readArguments(action, (Map<String, Object>) ((Map<String, Object>) value).get("input"), true);
				readArguments(action, (Map<String, Object>) ((Map<String, Object>) value).get("output"), false);
			});
		}
		return model;
	}

	@SuppressWarnings("unchecked")
	private void readArguments(ActionModel action, Map<String, Object> arguments, boolean isInput) {
		if (arguments == null) {
			return;
		}
		arguments.forEach((name, value) -> action.add(isInput, variable(name, (Map<String, Object>) value)));
	}

	private VariableModel variable(String name, Map<String, Object> value) {
		return new VariableModel(name, (String) value.get("java"), (String) value.get("upnp"));
	}
}
