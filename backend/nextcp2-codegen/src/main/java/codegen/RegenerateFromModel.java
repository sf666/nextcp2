package codegen;

/**
 * Renders the generated UPnP classes again from the stored service models.
 *
 * Run it after a template change - no device has to be reachable, because the models in the source
 * tree already hold everything that was ever discovered:
 *
 * <pre>
 * java -cp nextcp2-codegen/target/classes:$(cat cp.txt) codegen.RegenerateFromModel \
 *      "$PWD/nextcp2-modelgen/src/main/java"
 * </pre>
 */
public class RegenerateFromModel {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("usage: RegenerateFromModel <path to generated sources root>");
			System.exit(1);
		}
		String path = args[0];
		int count = new UpnpModelGen(new ICodegenConfig() {

			@Override
			public boolean isGenerateUpnpCode() {
				return true;
			}

			@Override
			public String getGenerateUpnpCodePath() {
				return path;
			}
		}).regenerateAll();
		System.out.println("regenerated services : " + count);
	}
}
