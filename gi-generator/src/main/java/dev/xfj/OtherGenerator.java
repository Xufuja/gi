package dev.xfj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

            Set<Node> nodes = traverseAll(jsonNode, new HashSet<>(), null);
            Map<String, Set<Node>> arrays = new HashMap<>();
            nodes.forEach(entry -> {
                if (entry.path().contains("[i].")) {
                    String[] split = entry.path().split("\\[i\\]");
                    String current = split[0];

                    if (!arrays.containsKey(current)) {
                        arrays.put(current, new HashSet<>());
                    }

                    arrays.get(current).add(new Node(split[1], entry.type()));
                } else {
                    String[] split = entry.path().split("\\.");

                    if (split.length > 2) {
                        for (int i = 0; i < split.length - 1; i++) {
                            if (!split[i].isEmpty()) {
                                System.out.println(format("%s=%s", split[i], JsonNodeType.OBJECT));
                            }
                        }
                    }
                }
            });
            System.out.println(arrays);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static Set<Node> traverseAll(JsonNode json, Set<Node> list, String node) {
        String currentNode = node != null ? node : ".";

        Iterator<String> fieldNames = json.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode sourceValue = json.get(fieldName);

            switch (sourceValue.getNodeType()) {
                case OBJECT -> traverseAll(sourceValue, list, format("%s%s.", currentNode, fieldName));
                case ARRAY -> {
                    if (sourceValue.size() == 0) {
                        list.add(new Node(format("%s%s[i]", currentNode, fieldName), JsonNodeType.STRING));
                    }
                    for (int i = 0; i < sourceValue.size(); i++) {
                        JsonNode current = sourceValue.get(i);
                        if (current.isObject() || current.isArray()) {
                            traverseAll(current, list, format("%s%s[i].", currentNode, fieldName));
                        } else {
                            list.add(new Node(format("%s%s[i]", currentNode, fieldName), current.getNodeType()));
                            break;
                        }
                    }
                }
                default -> list.add(new Node(format("%s%s", currentNode, fieldName), sourceValue.getNodeType()));
            }
        }

        return list;
    }
}
