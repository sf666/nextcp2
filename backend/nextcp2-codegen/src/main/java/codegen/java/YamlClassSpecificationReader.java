package codegen.java;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

public class YamlClassSpecificationReader
{
    public List<ClassSpecification> read(File yamlFile) throws FileNotFoundException
    {
        return read(new FileReader(yamlFile));
    }

    public List<ClassSpecification> read(Reader reader)
    {
        Map<String, Map<String, String>> yamlClassSpecifications = readYamlClassSpecifications(reader);
        List<ClassSpecification> classSpecifications = createClassSpecificationsFrom(yamlClassSpecifications);
        return classSpecifications;
    }

    public List<ClassSpecification> read(InputStream is)
    {
        Map<String, Map<String, String>> yamlClassSpecifications = readYamlClassSpecifications(is);
        List<ClassSpecification> classSpecifications = createClassSpecificationsFrom(yamlClassSpecifications);
        return classSpecifications;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> readYamlClassSpecifications(Reader reader)
    {
        Yaml yaml = new Yaml();
        Map<String, Map<String, String>> yamlClassSpecifications = (Map<String, Map<String, String>>) yaml.load(reader);
        return yamlClassSpecifications;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> readYamlClassSpecifications(InputStream is)
    {
        Yaml yaml = new Yaml();
        Map<String, Map<String, String>> yamlClassSpecifications = (Map<String, Map<String, String>>) yaml.load(is);
        return yamlClassSpecifications;
    }

    private List<ClassSpecification> createClassSpecificationsFrom(Map<String, Map<String, String>> yamlClassSpecifications)
    {
        final Map<String, List<FieldSpecification>> classNameToFieldSpecificationsMap = createClassNameToFieldSpecificationsMap(yamlClassSpecifications);

        List<ClassSpecification> classSpecifications = classNameToFieldSpecificationsMap.entrySet().stream().map(e -> new ClassSpecification(e.getKey(), e.getValue()))
                .collect(toList());

        return classSpecifications;
    }

    private Map<String, List<FieldSpecification>> createClassNameToFieldSpecificationsMap(Map<String, Map<String, String>> yamlClassSpecifications)
    {

        if (yamlClassSpecifications == null)
            return new HashMap<>();

        return yamlClassSpecifications.entrySet().stream().collect(toMap(this::className, this::fieldSpecifications));
    }

    private String className(Entry<String, Map<String, String>> yamlOuterMapEntry)
    {
        return yamlOuterMapEntry.getKey();
    }

    private List<FieldSpecification> fieldSpecifications(Entry<String, Map<String, String>> yamlOuterMapEntry)
    {
        Map<String, String> yamlFieldSpecifications = yamlOuterMapEntry.getValue();

        if (yamlFieldSpecifications == null)
            return new ArrayList<>();

        return yamlFieldSpecifications.entrySet().stream().map(e -> new FieldSpecification(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
}