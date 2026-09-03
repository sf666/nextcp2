package codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.jupnp.UpnpService;
import org.jupnp.model.message.header.STAllHeader;
import org.jupnp.model.meta.Action;
import org.jupnp.model.meta.ActionArgument;
import org.jupnp.model.meta.LocalDevice;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.meta.Service;
import org.jupnp.model.meta.StateVariable;
import org.jupnp.registry.Registry;
import org.jupnp.registry.RegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import codegen.model.ActionModel;
import codegen.model.ServiceModel;
import codegen.model.ServiceModelStore;
import codegen.model.VariableModel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

@Component
public class UpnpModelGen implements RegistryListener {

	private String basePackage = "nextcp.upnp.modelGen";

	public final static String MEDIA_SERVER_TYPE = "MediaServer";

	private static final Logger log = LoggerFactory.getLogger(UpnpModelGen.class.getName());

	private Configuration configuration;

	private final ServiceModelStore modelStore = new ServiceModelStore();

	@Autowired
	private UpnpService upnpService = null;

	@Autowired
	private ICodegenConfig config = null;

	public UpnpModelGen(ICodegenConfig config) {
		this();
		this.config = config;
	}

	public UpnpModelGen() {
		configuration = new Configuration(Configuration.VERSION_2_3_30);

		configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/template");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		configuration.setLogTemplateExceptions(true);
		configuration.setWrapUncheckedExceptions(true);
	}

	/**
	 * Renders every generated artifact again from the stored models, without touching the network.
	 * Needed whenever a template changes: the generated code is a function of the model, so it must
	 * be possible to reproduce all of it without waiting for every device to show up again.
	 *
	 * @return number of services that were regenerated.
	 */
	public int regenerateAll() {
		Path root = Path.of(config.getGenerateUpnpCodePath(), basePackage.replace(".", File.separator));
		if (!Files.isDirectory(root)) {
			log.error("[UpnpModelGen] no generated code at {}", root);
			return 0;
		}
		int count = 0;
		try (Stream<Path> files = Files.walk(root)) {
			for (Path file : files.filter(f -> f.getFileName().toString().equals(ServiceModelStore.FILE_NAME)).toList()) {
				ServiceModel model = modelStore.read(file.getParent().toFile());
				if (model == null) {
					log.error("[UpnpModelGen] cannot read {}", file);
					continue;
				}
				generate(model);
				count++;
			}
		} catch (IOException e) {
			log.error("[UpnpModelGen] cannot walk {}", root, e);
		}
		log.info("[UpnpModelGen] regenerated {} services.", count);
		return count;
	}

	@EventListener
	public void onApplicationStartedEvent(ContextRefreshedEvent event) {
		log.info("[UpnpModelGen] code generation for upnp services is " + (config.isGenerateUpnpCode() ? "enabled" : "disabled"));
	}

	@PostConstruct
	private void init() {
		if (config.isGenerateUpnpCode()) {
			log.info("[UpnpModelGen] starting control point RegistryListener ... ");
			upnpService.getRegistry().addListener(this);

			// Broadcast a search message for all devices
			upnpService.getControlPoint().search(new STAllHeader());
		}
	}

	private void dumpDeviceServices(RemoteDevice device) {
		log.info("[UpnpModelGen] Device named : " + device.getDisplayString());
		log.info("[UpnpModelGen]  available services :");
		for (RemoteService service : device.getServices()) {
			log.info(String.format("[UpnpModelGen]    - %s", service.getServiceType()));
			mergeAndGenerate(toModel(service));
		}
	}

	/**
	 * Reads what a device announces into a model of its own.
	 */
	private ServiceModel toModel(RemoteService service) {
		ServiceModel model = new ServiceModel(service.getServiceType().getNamespace(), service.getServiceType().getType(),
			service.getServiceType().getVersion());
		model.setEvents(service.getEventSubscriptionURI() != null);

		for (StateVariable<?> state : service.getStateVariables()) {
			log.info("[UpnpModelGen]    " + state.getName() + " : " + state.getTypeDetails().getDatatype().getDisplayString());
			if (!state.getName().startsWith("A_ARG_TYPE_")) {
				model.addStateVariable(toModel(new Variable(state)));
			}
		}
		for (Action<?> action : service.getActions()) {
			log.info(String.format("[UpnpModelGen]      * %s", action.getName()));
			ActionModel actionModel = model.action(action.getName());
			for (ActionArgument<?> argument : action.getInputArguments()) {
				actionModel.add(true, toModel(new Variable(argument)));
			}
			for (ActionArgument<?> argument : action.getOutputArguments()) {
				actionModel.add(false, toModel(new Variable(argument)));
			}
		}
		return model;
	}

	private VariableModel toModel(Variable variable) {
		return new VariableModel(variable.getName(), variable.getType(), variable.getUpnpType());
	}

	/**
	 * Adds what this device can do to what the model already knows and regenerates from the union.
	 * The stored model wins on a type conflict - the generated code converts incoming values instead
	 * of casting them, so a disagreement is worth reporting but does not need a decision here.
	 */
	private synchronized void mergeAndGenerate(ServiceModel discovered) {
		// Devices are announced on several threads at once, and two of them offering the same service
		// type would otherwise read and write the same model file concurrently: a torn read looks
		// like "no model yet" and would rebuild the union from that single device.
		File directory = new File(getDirectory(discovered));
		ServiceModel model = modelStore.read(directory);
		if (model == null) {
			model = new ServiceModel(discovered.getNamespace(), discovered.getServiceType(), discovered.getVersion());
		}

		ServiceModel.MergeResult result = model.mergeFrom(discovered);
		for (String conflict : result.conflicts()) {
			log.error("[UpnpModelGen] {}:{} announces a conflicting type - keeping the known one : {}",
				discovered.getServiceType(), discovered.getVersion(), conflict);
		}
		if (result.changed()) {
			log.info("[UpnpModelGen] {}:{} gained new elements.", discovered.getServiceType(), discovered.getVersion());
		}

		modelStore.write(directory, model);
		generate(model);
	}

	/**
	 * Writes every generated artifact of one service from the model.
	 */
	private void generate(ServiceModel model) {
		genServiceClass(model);
		genStateVariableClass(model);
		for (ActionModel action : model.getActions().values()) {
			genParamClass(model, action, true);
			genParamClass(model, action, false);
			genActionClass(model, action);
		}
	}

	private void genServiceClass(ServiceModel model) {
		String className = identifier(model.getServiceType()) + "Service";
		Map<String, Object> root = new HashMap<>();
		root.put("className", className);
		root.put("upnpSchema", model.getNamespace());
		root.put("upnpService", model.getServiceType());
		root.put("packageName", getPackage(model));
		List<String> inputClasses = new ArrayList<>();
		List<String> outputClasses = new ArrayList<>();
		List<String> importClasses = new ArrayList<>();
		root.put("inputClasses", inputClasses);
		root.put("outputClasses", outputClasses);
		root.put("importClasses", importClasses);
		root.put("stateVariableClassName", getStateVariableClassname(model));
		root.put("stateVariables", toVariables(model.getStateVariableList()));

		List<String> actionNames = new ArrayList<>();
		root.put("actionNames", actionNames);
		String actionPackage = getActionPackage(model);
		for (ActionModel action : model.getActions().values()) {
			actionNames.add(action.getName());
			importClasses.add(actionPackage + "." + action.getName());
			if (!action.getOutput().isEmpty()) {
				outputClasses.add(action.getName() + "Output");
				importClasses.add(actionPackage + "." + action.getName() + "Output");
			}
			if (!action.getInput().isEmpty()) {
				inputClasses.add(action.getName() + "Input");
				importClasses.add(actionPackage + "." + action.getName() + "Input");
			}
		}

		writeCode(root, getFilename(model, className), "service.ftl");
		writeCode(root, getFilename(model, "I" + className + "EventListener"), "serviceEventInterace.ftl");
		writeCode(root, getFilename(model, className + "EventListenerImpl"), "serviceEventImpl.ftl");
		if (model.hasEvents()) {
			writeCode(root, getFilename(model, className + "Subscription"), "serviceSubscription.ftl");
		} else {
			log.debug("Service has no subscription : " + model.getServiceType());
		}
	}

	private List<Variable> toVariables(List<VariableModel> variables) {
		List<Variable> result = new ArrayList<>();
		for (VariableModel variable : variables) {
			result.add(new Variable(variable.getName(), variable.getJavaType(), variable.getUpnpType()));
		}
		return result;
	}

	private void genActionClass(ServiceModel model, ActionModel action) {
		Map<String, Object> root = new HashMap<>();
		root.put("className", action.getName());
		root.put("varOutList", toVariables(action.getOutputList()));
		root.put("varInList", toVariables(action.getInputList()));
		root.put("packageName", getActionPackage(model));

		writeCode(root, getActionFilename(model, action.getName(), ""), "action.ftl");
	}

	private String getPackage(ServiceModel model) {
		return String.format("%s.%s.%s%d", basePackage, getNamespace(model),
			toLowerFirstCap(identifier(model.getServiceType())), model.getVersion());
	}

	private String getActionPackage(ServiceModel model) {
		return getPackage(model) + ".actions";
	}

	private String getNamespace(ServiceModel model) {
		return model.getNamespace().replaceAll("-", "").toLowerCase();
	}

	private String getStateVariableClassname(ServiceModel model) {
		return String.format("%sServiceStateVariable", identifier(model.getServiceType()));
	}

	protected String getDirectory(ServiceModel model) {
		String replacedPackagename = getPackage(model).replaceAll("\\.", File.separator);
		return FilenameUtils.concat(config.getGenerateUpnpCodePath(), replacedPackagename);
	}

	protected String getFilename(ServiceModel model, String className) {
		return FilenameUtils.concat(getDirectory(model), className + ".java");
	}

	protected String getActionFilename(ServiceModel model, String actionName, String postFix) {
		String path = FilenameUtils.concat(getDirectory(model), "actions");
		return FilenameUtils.concat(path, actionName + postFix + ".java");
	}

	/**
	 * A service type is free to contain characters that a java identifier cannot (webos-second-screen
	 * on an LG TV). Separators are dropped and the following letter capitalised, which leaves every
	 * conventional type untouched. The raw type is still what the generated code hands to
	 * ServiceType, so lookups keep working.
	 */
	private String identifier(String serviceType) {
		StringBuilder sb = new StringBuilder();
		boolean capitalizeNext = false;
		for (char c : serviceType.toCharArray()) {
			if (Character.isLetterOrDigit(c) || c == '_') {
				sb.append(capitalizeNext ? Character.toUpperCase(c) : c);
				capitalizeNext = false;
			} else {
				capitalizeNext = sb.length() > 0;
			}
		}
		return sb.toString();
	}

	private String toLowerFirstCap(String string) {
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	private void genParamClass(ServiceModel model, ActionModel action, boolean isInput) {
		List<VariableModel> args = isInput ? action.getInputList() : action.getOutputList();
		if (args.isEmpty()) {
			return;
		}
		String postfix = isInput ? "Input" : "Output";
		Map<String, Object> root = new HashMap<>();
		root.put("className", action.getName() + postfix);
		root.put("varList", toVariables(args));
		root.put("packageName", getActionPackage(model));

		writeCode(root, getActionFilename(model, action.getName(), postfix), "actionParam.ftl");
	}

	private void genStateVariableClass(ServiceModel model) {
		// The class is written even without variables: the generated event listener implementation
		// references it unconditionally, so skipping it leaves a package that does not compile. A
		// service whose evented variables are all A_ARG_TYPE_* (QPlay:2) hits exactly that case.
		Map<String, Object> root = new HashMap<>();
		root.put("className", getStateVariableClassname(model));
		root.put("varList", toVariables(model.getStateVariableList()));
		root.put("packageName", getPackage(model));

		writeCode(root, getFilename(model, getStateVariableClassname(model)), "actionParam.ftl");
	}

	private void writeCode(Map<String, Object> input, String filename, String templatePath) {
		try {
			Template template = configuration.getTemplate(templatePath);
			StringWriter rendered = new StringWriter();
			template.process(input, rendered);

			File genFile = new File(filename);
			if (isUnchanged(genFile, rendered.toString())) {
				log.debug("[UpnpModelGen] unchanged : {}", genFile.getAbsolutePath());
				return;
			}
			if (!genFile.getParentFile().exists() && !genFile.getParentFile().mkdirs()) {
				log.warn("[UpnpModelGen] mkdirs failed for {}", genFile.getParentFile().getAbsolutePath());
			}
			log.info("[UpnpModelGen] Generate file at path : {} ", genFile.getAbsolutePath());
			try (Writer fileWriter = new FileWriter(genFile)) {
				fileWriter.write(rendered.toString());
			}
		} catch (Exception e) {
			log.error("[UpnpModelGen] cannot generate {}", filename, e);
		}
	}

	/**
	 * Rewriting identical content would mark every generated file as touched on each run and bury
	 * the actual gain in the diff.
	 */
	private boolean isUnchanged(File file, String content) {
		try {
			return file.isFile() && Files.readString(file.toPath()).equals(content);
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {

	}

	@Override
	public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {

	}

	@Override
	public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
		dumpDeviceServices(device);
	}

	@Override
	public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {

	}

	@Override
	public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {

	}

	@Override
	public void localDeviceAdded(Registry registry, LocalDevice device) {

	}

	@Override
	public void localDeviceRemoved(Registry registry, LocalDevice device) {

	}

	@Override
	public void beforeShutdown(Registry registry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterShutdown() {
		// TODO Auto-generated method stub

	}

}
