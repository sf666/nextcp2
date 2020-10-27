package codegen;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import codegen.java.ClassSpecification;
import codegen.java.JavaDataClassGenerator;
import codegen.java.YamlClassSpecificationReader;

public class DtoModelGen
{

    public void generate(InputStream yamlFile, File outputDirectory, String packageName, String freemarkerTemplateFolder) throws Exception
    {
        YamlClassSpecificationReader yamlReader = new YamlClassSpecificationReader();
        List<ClassSpecification> classSpecifications = yamlReader.read(yamlFile);

        JavaDataClassGenerator javaDataClassGenerator = new JavaDataClassGenerator(freemarkerTemplateFolder, packageName);

        javaDataClassGenerator.generateJavaSourceFiles(classSpecifications, outputDirectory);

    }

    public static void main(String[] args) throws Exception
    {
    	if (args.length != 1)
    	{
    		System.out.println("call main[] with an argument: src-base directory of nextcp2-modelgen project.");
    	}
        System.out.println("start generating DTO's");
        System.out.println("using directory : " + args[0]);
        DtoModelGen modelGen = new DtoModelGen();
        File directory = new File(args[0]);
        if (!directory.exists())
        {
            directory.mkdirs();
        }
        modelGen.generate(DtoModelGen.class.getResourceAsStream("/yaml/dto.yaml"), directory, "nextcp.dto", "/template");
        System.out.println("finished.");
    }
}
