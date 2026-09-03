package codegen;

import org.jupnp.UpnpService;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.model.message.header.STAllHeader;

import main.Nextcp2DefaultUpnpServiceConfiguration;

/**
 * Searches the local network and merges what every device announces into the stored service models.
 *
 * Same effect as running the code generator inside the application, but without the Spring context:
 *
 * <pre>
 * java -cp nextcp2-codegen/target/classes:$(cat cp.txt) codegen.HarvestFromNetwork \
 *      "$PWD/nextcp2-modelgen/src/main/java" 120
 * </pre>
 *
 * The second argument is how many seconds to keep listening. Devices answer a search within a few
 * seconds, but the longer this runs the more of them are caught - and since the models only ever
 * grow, running it again later can only add.
 */
public class HarvestFromNetwork {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("usage: HarvestFromNetwork <path to generated sources root> [seconds]");
			System.exit(1);
		}
		String path = args[0];
		long seconds = args.length > 1 ? Long.parseLong(args[1]) : 90;

		ICodegenConfig config = new ICodegenConfig() {

			@Override
			public boolean isGenerateUpnpCode() {
				return true;
			}

			@Override
			public String getGenerateUpnpCodePath() {
				return path;
			}
		};

		UpnpService upnpService = new UpnpServiceImpl(new Nextcp2DefaultUpnpServiceConfiguration());
		upnpService.startup();
		try {
			upnpService.getRegistry().addListener(new UpnpModelGen(config));
			upnpService.getControlPoint().search(new STAllHeader());
			System.out.println("searching for " + seconds + " seconds ...");
			Thread.sleep(seconds * 1000);
		} finally {
			upnpService.shutdown();
		}
		System.out.println("done.");
	}
}
