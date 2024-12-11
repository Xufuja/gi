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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassGenerator {
    private final String dataDirectory;
    private final String outputDirectory;

    public ClassGenerator(String dataDirectory, String outputDirectory) {
        this.dataDirectory = dataDirectory;
        this.outputDirectory = outputDirectory;
    }

    private String prepare(String path, boolean array) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(path);
        JsonNode jsonNode = objectMapper.readTree(file);

        if (array) {
            ObjectNode result = objectMapper.createObjectNode();

            for (JsonNode node : jsonNode) {
                if (node.isObject()) {
                    mergeJsonNodes(result, (ObjectNode) node);
                }
            }

            return result.toPrettyString();
        } else {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readTree(file));
        }
    }

    private static void mergeJsonNodes(ObjectNode target, ObjectNode source) {
        Iterator<String> fieldNames = source.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode sourceValue = applyOverride(fieldName, source.get(fieldName));

            if (target.has(fieldName)) {
                JsonNode targetValue = target.get(fieldName);

                if (targetValue.isObject() && sourceValue.isObject()) {
                    mergeJsonNodes((ObjectNode) targetValue, (ObjectNode) sourceValue);
                } else if (targetValue.isArray() && sourceValue.isArray()) {
                    mergeArrayNodes((ArrayNode) targetValue, (ArrayNode) sourceValue);
                } else {
                    target.set(fieldName, sourceValue);
                }
            } else {
                target.set(fieldName, sourceValue);
            }
        }
    }

    private static JsonNode applyOverride(String fieldName, JsonNode node) {
        ObjectMapper mapper = new ObjectMapper();
        //Since it merges all array items, the last item overrides these 3 as integers while prior ones are floats
        if (fieldName.equals("hpBase") || fieldName.equals("attackBase") || fieldName.equals("defenseBase")) {
            return new FloatNode(1.1f);
        } else if (fieldName.endsWith("Hash")) { //Seems it originally uses numeric strings that look like longs
            return new TextNode("a");
        } else if (fieldName.equals("paramDescList")) {
            ArrayNode arrayNode = mapper.createArrayNode();
            return arrayNode.add("a");
        } else if (fieldName.equals("paramList")) {
            ArrayNode arrayNode = mapper.createArrayNode();
            return arrayNode.add(1.1f);
        }

        return node;
    }

    private static void mergeArrayNodes(ArrayNode targetArray, ArrayNode sourceArray) {
        int maxSize = Math.max(targetArray.size(), sourceArray.size());

        for (int i = 0; i < maxSize; i++) {
            JsonNode targetElement = i < targetArray.size() ? targetArray.get(i) : null;
            JsonNode sourceElement = i < sourceArray.size() ? sourceArray.get(i) : null;

            if (targetElement != null && sourceElement != null) {
                if (targetElement.isObject() && sourceElement.isObject()) {
                    mergeJsonNodes((ObjectNode) targetElement, (ObjectNode) sourceElement);
                } else if (targetElement.isArray() && sourceElement.isArray()) {
                    mergeArrayNodes((ArrayNode) targetElement, (ArrayNode) sourceElement);
                } else {
                    targetArray.set(i, sourceElement);
                }
            } else if (sourceElement != null) {
                targetArray.add(sourceElement);
            }
        }
    }

    public void createClasses() throws IOException {
        createClasses(FileFilter.NONE, true);
    }

    public void createClasses(FileFilter fileFilter, boolean array) throws IOException {
        Set<String> set = getAllFiles(fileFilter);

        for (String item : set) {
            if (!Character.isDigit(item.charAt(0))) {
                System.out.println("Generating for: " + item);
                String json = prepare(dataDirectory + item, array);
                System.out.println("Input JSON for generator:\r\n" + json);
                createClass(
                        json,
                        new File(outputDirectory),
                        "dev.xfj.jsonschema2pojo." + item.replace(".json", "").toLowerCase(),
                        item
                );
            } else {
                System.out.println("Skipping generation for: " + item);
            }
        }
    }

    private void createClass(String jsonString, File outputDirectory, String packageName, String className)
            throws IOException {
        JCodeModel jCodeModel = new JCodeModel();

        GenerationConfig config = new DefaultGenerationConfig() {
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
        };

        SchemaMapper mapper = new SchemaMapper(
                new RuleFactory(config, new GsonAnnotator(config), new SchemaStore()),
                new SchemaGenerator()
        );

        mapper.generate(jCodeModel, className, packageName, jsonString);

        jCodeModel.build(outputDirectory);
    }

    private Set<String> getAllFiles(FileFilter fileFilter) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dataDirectory))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(file -> !filterOut(file, fileFilter))
                    .collect(Collectors.toSet());
        }
    }

    private boolean filterOut(String string, FileFilter fileFilter) {
        return switch (fileFilter) {
            case NONE -> false;
            case MORE_THAN_1_UNDERSCORE -> string.chars().filter(character -> character == '_').count() > 1;
            case CHIORI_TEST -> !string.equals("ConfigAvatar_Chiori.json");
        };
    }
}
