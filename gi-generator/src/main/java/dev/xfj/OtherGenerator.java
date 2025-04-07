package dev.xfj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class OtherGenerator {
    private final String dataDirectory;
    private final String outputDirectory;

    public OtherGenerator(String dataDirectory, String outputDirectory) {
        this.dataDirectory = dataDirectory;
        this.outputDirectory = outputDirectory;
    }

    public void createClasses() throws IOException {
        createClasses(PatternFilter.NONE, true);
    }

    public void createClasses(PatternFilter patternFilter, boolean array) throws IOException {
        Set<String> set = getAllFiles(patternFilter);

        for (String item : set) {
            if (!Character.isDigit(item.charAt(0))) {
                System.out.println("Generating for: " + item);
                String json = prepare(dataDirectory + item, array);
                System.out.println("Input JSON for generator:\r\n" + json);
                createClass(
                        json,
                        new File(outputDirectory),
                        "dev.xfj.generated." + item.replace(".json", "").toLowerCase(),
                        item
                );
            } else {
                System.out.println("Skipping generation for: " + item);
            }
        }
    }

    public void createClassesAvatarConfig() throws IOException {
        createClassesFromMultipleObjects(
                PatternFilter.MORE_THAN_1_UNDERSCORE,
                name -> name.startsWith("ConfigAvatar"),
                name -> name.split("_")[0] + ".json"
        );
    }

    public void createClassesAvatarAbilities() throws IOException {
        createClassesFromMultipleObjects(
                PatternFilter.NONE,
                name -> name.startsWith("ConfigAbility_Avatar"),
                name -> {
                    String[] split = name.split("_");
                    return format("%s%s.json", split[0], split[1]);
                }
        );
    }

    public void createClassesFromMultipleObjects(
            PatternFilter patternFilter,
            Predicate<String> nameFilter,
            Function<String, String> namePattern
    ) throws IOException {
        Set<String> set = getAllFiles(patternFilter);
        String name = "";

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();

        for (String item : set) {
            if (nameFilter.test(item)) {
                System.out.println("Adding to array: " + item);
                if (name.isEmpty()) {
                    name = namePattern.apply(item);
                }

                File file = new File(dataDirectory + item);
                JsonNode jsonNode = objectMapper.readTree(file);

                switch (jsonNode.getNodeType()) {
                    case OBJECT -> arrayNode.add(objectMapper.readTree(file));
                    case ARRAY -> {
                        for (JsonNode node : jsonNode) {
                            arrayNode.add(node);
                        }
                    }
                    default -> System.err.println("Unhandled node: " + jsonNode.getNodeType());
                }
            } else {
                System.out.println("Skipping generation for: " + item);
            }
        }

        String json = prepare(objectMapper, arrayNode, name);
        System.out.println("Input JSON for generator:\r\n" + json);
        createClass(
                json,
                new File(outputDirectory),
                "dev.xfj.generated." + name.replace(".json", "").toLowerCase(),
                name.replace(".json", ""),
                new DefaultGenerationConfig() {
                    @Override
                    public SourceType getSourceType() {
                        return SourceType.JSON;
                    }

                    @Override
                    public boolean isGenerateBuilders() {
                        return true;
                    }

                    @Override
                    public boolean isUsePrimitives() {
                        return true;
                    }

                    @Override
                    public char[] getPropertyWordDelimiters() {
                        return new char[]{'-', ' '};
                    }
                }
        );
    }

    private String prepare(String path, boolean array) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(path);
        String[] split = path.split("\\\\");
        String name = split[split.length - 1];

        if (array) {
            return prepare(objectMapper, objectMapper.readTree(file), name);
        } else {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readTree(file));
        }
    }

    private String prepare(ObjectMapper objectMapper, JsonNode jsonNode, String name) {
        ObjectNode result = objectMapper.createObjectNode();

        for (JsonNode node : jsonNode) {
            if (node.isObject()) {
                mergeJsonNodes(result, (ObjectNode) node, name);
            }
        }

        return result.toPrettyString();
    }

    private static void mergeJsonNodes(ObjectNode target, ObjectNode source, String file) {
        Iterator<String> fieldNames = source.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode sourceValue = applyValueOverride(fieldName, source.get(fieldName), file);

            if (target.has(fieldName)) {
                JsonNode targetValue = target.get(fieldName);

                if (targetValue.isObject() && sourceValue.isObject()) {
                    mergeJsonNodes((ObjectNode) targetValue, (ObjectNode) sourceValue, file);
                } else if (targetValue.isArray() && sourceValue.isArray()) {
                    mergeArrayNodes((ArrayNode) targetValue, (ArrayNode) sourceValue, file);
                } else {
                    target.set(fieldName, sourceValue);
                }
            } else {
                target.set(fieldName, sourceValue);
            }
        }
    }


    private static JsonNode applyValueOverride(String fieldName, JsonNode node, String file) {
        ObjectMapper mapper = new ObjectMapper();
        //Since it merges all array items, the last item overrides these 3 as integers while prior ones are floats
        if (fieldName.equals("hpBase") || fieldName.equals("attackBase") || fieldName.equals("defenseBase")) {
            return new FloatNode(1.1f);
        } else if (fieldName.endsWith("Hash")) { //Seems it originally uses numeric strings that look like longs
            return new TextNode("a");
        } else if (
                fieldName.equals("paramDescList") ||
                        fieldName.equals("tips") ||
                        fieldName.equals("jumpDescs") ||
                        fieldName.equals("textList")
        ) {
            ArrayNode arrayNode = mapper.createArrayNode();
            return arrayNode.add("a");
        } else if (fieldName.equals("paramList")) {
            if (file.equals("AchievementExcelConfigData.json")) {
                ArrayNode arrayNode = mapper.createArrayNode();
                return arrayNode.add("a");
            } else {
                ArrayNode arrayNode = mapper.createArrayNode();
                return arrayNode.add(1.1f);
            }
        }

        return node;
    }

    private static void mergeArrayNodes(ArrayNode targetArray, ArrayNode sourceArray, String file) {
        int maxSize = Math.max(targetArray.size(), sourceArray.size());

        for (int i = 0; i < maxSize; i++) {
            JsonNode targetElement = i < targetArray.size() ? targetArray.get(i) : null;
            JsonNode sourceElement = i < sourceArray.size() ? sourceArray.get(i) : null;

            if (targetElement != null && sourceElement != null) {
                if (targetElement.isObject() && sourceElement.isObject()) {
                    mergeJsonNodes((ObjectNode) targetElement, (ObjectNode) sourceElement, file);
                } else if (targetElement.isArray() && sourceElement.isArray()) {
                    mergeArrayNodes((ArrayNode) targetElement, (ArrayNode) sourceElement, file);
                } else {
                    targetArray.set(i, sourceElement);
                }
            } else if (sourceElement != null) {
                targetArray.add(sourceElement);
            }
        }
    }

    private void createClass(
            String jsonString,
            File outputDirectory,
            String packageName,
            String className
    ) throws IOException {
        createClass(
                jsonString,
                outputDirectory,
                packageName,
                className,
                new DefaultGenerationConfig() {
                    @Override
                    public SourceType getSourceType() {
                        return SourceType.JSON;
                    }

                    @Override
                    public boolean isGenerateBuilders() {
                        return true;
                    }

                    @Override
                    public boolean isUsePrimitives() {
                        return true;
                    }
                });
    }

    private void createClass(
            String jsonString,
            File outputDirectory,
            String packageName,
            String className,
            GenerationConfig generationConfig
    ) throws IOException {
        JCodeModel jCodeModel = new JCodeModel();

        SchemaMapper mapper = new SchemaMapper(
                new RuleFactory(generationConfig, new GsonAnnotator(generationConfig), new SchemaStore()),
                new org.jsonschema2pojo.SchemaGenerator()
        );

        mapper.generate(jCodeModel, className, packageName, jsonString);

        jCodeModel.build(outputDirectory);
    }

    private Set<String> getAllFiles(PatternFilter patternFilter) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dataDirectory))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(file -> !patternFilter.filterOut(file))
                    .collect(Collectors.toSet());
        }
    }

    public void createSchema(String value) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(value);

            traverseAll(jsonNode, null);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void traverseAll(JsonNode json, String node) {
        String currentNode = node != null ? node : ".";

        Iterator<String> fieldNames = json.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode sourceValue = json.get(fieldName);

            switch (sourceValue.getNodeType()) {
                case OBJECT -> {
                    traverseAll(sourceValue, format("%s%s.", currentNode, fieldName));
                }
                case ARRAY -> {
                    if (sourceValue.size() == 0) {
                        System.out.println(format("%s%s[i]=%s", currentNode, fieldName, sourceValue.getNodeType()));
                    }
                    for (int i = 0; i < sourceValue.size(); i++) {
                        JsonNode current = sourceValue.get(i);
                        if (current.isObject() || current.isArray()) {
                            traverseAll(current, format("%s%s[i].", currentNode, fieldName));
                        } else {
                            System.out.println(format("%s%s[i]=%s", currentNode, fieldName, current.getNodeType()));
                            break;
                        }
                    }
                }
                default -> {
                    System.out.println(format("%s%s=%s", currentNode, fieldName, sourceValue.getNodeType()));
                }
            }
        }
    }
}
