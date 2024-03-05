package codegen;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import codegen.java.ClassSpecification;
import codegen.java.JavaDataClassGenerator;
import codegen.java.YamlClassSpecificationReader;

public class DtoModelGen {

	public void generate(InputStream yamlFile, File outputDirectory, String packageName, String freemarkerTemplateFolder) throws Exception {
		YamlClassSpecificationReader yamlReader = new YamlClassSpecificationReader();
		List<ClassSpecification> classSpecifications = yamlReader.read(yamlFile);

		JavaDataClassGenerator javaDataClassGenerator = new JavaDataClassGenerator(freemarkerTemplateFolder, packageName);

		javaDataClassGenerator.generateJavaSourceFiles(classSpecifications, outputDirectory);

	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("call main[] with an argument: src-base directory of nextcp2-modelgen project like /dev/nextcp2/backend/nextcp2-modelgen/src/main/java/nextcp/dto");
		}
		System.out.println("start generating DTO's");
		System.out.println("using directory : " + args[0]);
		DtoModelGen modelGen = new DtoModelGen();
		modelGen.genCode(args[0]);
	}

	private void genCode(String genPath) throws Exception {
		File directory = new File(genPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String resourcePath = "/yaml/dto.yaml";
		InputStream is = getClass().getResourceAsStream(resourcePath);
		if (is == null) {
			throw new RuntimeException("inputstream empty. Path : " + resourcePath);
		}
		generate(is, directory, "nextcp.dto", "/template");
		System.out.println("finished.");
		
	}
}