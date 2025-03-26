package codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

@Component
public class UpnpModelGen implements RegistryListener {

	private String basePackage = "nextcp.upnp.modelGen";

	public final static String MEDIA_SERVER_TYPE = "MediaServer";

	private static final Logger log = LoggerFactory.getLogger(UpnpModelGen.class.getName());

	private Configuration configuration;

	@Autowired
	private UpnpService upnpService = null;

	@Autowired
	private ICodegenConfig config = null;

	public UpnpModelGen() {
		configuration = new Configuration(Configuration.VERSION_2_3_30);

		configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/template");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		configuration.setLogTemplateExceptions(true);
		configuration.setWrapUncheckedExceptions(true);
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

			for (Action<?> action : service.getActions()) {
				log.info(String.format("[UpnpModelGen]      * %s", action.getName()));

				genParamClass(action, true);
				genParamClass(action, false);
				genActionClass(action);
			}

			genServiceClass(service);
			genStateVariableClass(service);
		}
	}

	private void genServiceClass(RemoteService service) {
		String className = service.getServiceType().getType() + "Service";
		Map<String, Object> root = new HashMap<>();
		root.put("className", className);
		root.put("upnpSchema", service.getServiceType().getNamespace());
		root.put("upnpService", service.getServiceType().getType());
		root.put("packageName", getPackage(service));
		List<String> inputClasses = new ArrayList<>();
		List<String> outputClasses = new ArrayList<>();
		List<String> importClasses = new ArrayList<>();
		root.put("inputClasses", inputClasses);
		root.put("outputClasses", outputClasses);
		root.put("importClasses", importClasses);
		root.put("stateVariableClassName", getStateVariableClassname(service));
		root.put("stateVariables", getStateVariableList(service));

		List<String> actionNames = new ArrayList<>();
		root.put("actionNames", actionNames);
		for (Action action : service.getActions()) {
			actionNames.add(action.getName());
			importClasses.add(getPackage(action) + "." + action.getName());
			if (action.getOutputArguments().length > 0) {
				outputClasses.add(action.getName() + "Output");
				importClasses.add(getPackage(action) + "." + action.getName() + "Output");
			}
			if (action.getInputArguments().length > 0) {
				inputClasses.add(action.getName() + "Input");
				importClasses.add(getPackage(action) + "." + action.getName() + "Input");
			}
		}

		// Write Class
		String fn = getFilename(service, className);
		writeCode(root, fn, "service.ftl");

		// Write SubscriptionInterface
		fn = getFilename(service, "I" + className + "EventListener");
		writeCode(root, fn, "serviceEventInterace.ftl");

		// Write SubscriptionInterface Implementation
		fn = getFilename(service, className + "EventListenerImpl");
		writeCode(root, fn, "serviceEventImpl.ftl");

		// Generate event subscription class
		if (service.getEventSubscriptionURI() != null) {
			fn = getFilename(service, className + "Subscription");
			writeCode(root, fn, "serviceSubscription.ftl");
		} else {
			log.debug("Service has no subscription : " + service.getServiceType().getType().toString());
		}
	}

	private String getPackage(Service service) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.basePackage).append(".").append(getNamespace(service)).append(".");
		sb.append(toLowerFirstCap(getServiceType(service))).append(service.getServiceType().getVersion());
		return sb.toString();
	}

	public String getServiceType(Service service) {
		return service.getServiceType().getType();
	}

	public String getServiceType(Action<?> action) {
		return getServiceType(action.getService());
	}

	private void genActionClass(Action<?> action) {
		String className = action.getName();
		Map<String, Object> root = new HashMap<>();
		List<Variable> varOutList = new LinkedList<>();
		List<Variable> varInList = new LinkedList<>();
		root.put("className", className);
		root.put("varOutList", varOutList);
		root.put("varInList", varInList);
		root.put("packageName", getPackage(action));

		for (ActionArgument<?> argument : action.getOutputArguments()) {
			varOutList.add(new Variable(argument));
		}
		for (ActionArgument<?> argument : action.getInputArguments()) {
			varInList.add(new Variable(argument));
		}

		writeCode(root, getFilename(action, ""), "action.ftl");
	}

	protected String getPackage(Action<?> action) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.basePackage).append(".").append(getNamespace(action)).append(".");
		sb.append(toLowerFirstCap(action.getService().getServiceType().getType())).append(action.getService().getServiceType().getVersion())
			.append(".actions");
		return sb.toString();
	}

	protected String getFilename(String packageName, String className) {
		String replacedPackagename = packageName.replaceAll("\\.", File.separator);
		String path = FilenameUtils.concat(config.getGenerateUpnpCodePath(), replacedPackagename);
		path = FilenameUtils.concat(path, className + ".java");
		return path;
	}

	protected String getFilename(Action<?> action, String postFix) {
		String path = FilenameUtils.concat(getDirectory(action.getService()), "actions");
		path = FilenameUtils.concat(path, action.getName() + postFix + ".java");
		return path;
	}

	protected String getFilename(Service service, String className) {
		String path = FilenameUtils.concat(getDirectory(service), className + ".java");
		return path;
	}

	protected String getDirectory(Service service) {
		String packageName = getPackage(service);
		String replacedPackagename = packageName.replaceAll("\\.", File.separator);
		String path = FilenameUtils.concat(config.getGenerateUpnpCodePath(), replacedPackagename);
		return path;
	}

	private String getNamespace(Action<?> action) {
		String ns = getNamespace(action.getService());
		return ns;
	}

	private String getNamespace(Service service) {
		String ns = service.getServiceType().getNamespace().replaceAll("-", "").toLowerCase();
		return ns;
	}

	private String toLowerFirstCap(String string) {
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	private void genParamClass(Action<?> action, boolean isInput) {
		ActionArgument<?>[] args = null;
		String postfix = null;
		if (isInput) {
			postfix = "Input";
			args = action.getInputArguments();
			log.info("        INPUT");
		} else {
			args = action.getOutputArguments();
			postfix = "Output";
			log.info("        OUTPUT");
		}

		if (args.length > 0) {
			String className = action.getName() + postfix;
			Map<String, Object> root = new HashMap<>();
			List<Variable> varList = new LinkedList<>();
			root.put("className", className);
			root.put("varList", varList);
			root.put("packageName", getPackage(action));

			for (ActionArgument<?> argument : args) {
				log.info(String.format("        %s : %s.", argument.getName(), argument.getDatatype().toString()));
				varList.add(new Variable(argument));
			}

			writeCode(root, getFilename(action, postfix), "actionParam.ftl");
		}
	}

	private String getStateVariableClassname(Service service) {
		String className = String.format("%sServiceStateVariable", service.getServiceType().getType());
		return className;
	}

	private void genStateVariableClass(Service service) {
		log.info("[UpnpModelGen] State Variables");
		log.info("[UpnpModelGen] ==========================================================================");

		List<Variable> vars = getStateVariableList(service);

		if (vars.size() > 0) {
			String packageName = getPackage(service);
			String className = getStateVariableClassname(service);

			Map<String, Object> root = new HashMap<>();
			List<Variable> varList = new LinkedList<>();
			root.put("className", className);
			root.put("varList", vars);
			root.put("packageName", packageName);

			writeCode(root, getFilename(packageName, className), "actionParam.ftl");
		}
	}

	private List<Variable> getStateVariableList(Service service) {
		List<Variable> varList = new LinkedList<>();
		for (StateVariable<?> state : service.getStateVariables()) {
			log.info("[UpnpModelGen]    " + state.getName() + " : " + state.getTypeDetails().getDatatype().getDisplayString());
			if (!state.getName().startsWith("A_ARG_TYPE_")) {
				varList.add(new Variable(state));
			}
		}
		return varList;
	}

	private void writeCode(Map<String, Object> input, String filename, String templatePath) {
		Template template;
		try {
			template = configuration.getTemplate(templatePath);
			File genFile = new File(filename);
			if (!genFile.getParentFile().exists()) {
				if (!genFile.getParentFile().mkdirs()) {
					log.info("[UpnpModelGen] mkdirs failed!");					
				}
			}
			log.info("[UpnpModelGen] Generate file at path : {} ", genFile.getAbsolutePath());
			Writer fileWriter = new FileWriter(new File(filename));
			try {
				template.process(input, fileWriter);
			} finally {
				fileWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
