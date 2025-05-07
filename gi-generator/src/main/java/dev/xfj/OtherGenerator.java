package dev.xfj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

            Map<Node, Set<Node>> objects = new HashMap<>();

            nodes.forEach(entry -> {
                handleNode(entry, objects);
            });

            objects.entrySet().stream()
                    .filter(entry -> entry.getKey().type() == JsonNodeType.ARRAY)
                    .forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void handleNode(Node entry, Map<Node, Set<Node>> objects) {
        if (entry.path().contains("[i]")) {
            String[] split = entry.path().split("\\[i\\]");
            String current = split[0];

            Optional<Node> currentNode = objects.keySet()
                    .stream()
                    .filter(array -> array.path().contains(current))
                    .findFirst();

            if (currentNode.isPresent()) {
                if (split.length > 1) {
                    objects.get(currentNode.get()).add(new Node(split[1], entry.type(), entry.numberType()));
                } else {
                    objects.get(currentNode.get()).add(new Node(".", entry.type(), entry.numberType()));
                }
            } else {
                Set<Node> set = new HashSet<>();

                if (split.length > 1) {
                    set.add(new Node(split[1], entry.type(), entry.numberType()));
                } else {
                    set.add(new Node(".", entry.type(), entry.numberType()));
                }

                objects.put(new Node(current, JsonNodeType.ARRAY, null), set);
            }
        } else {
            String[] split = entry.path().split("\\.");

            if (split.length > 2) {
                for (int i = 0; i < split.length - 1; i++) {
                    if (!split[i].isEmpty()) {
                        String current = "." + split[i];

                        Optional<Node> currentNode = objects.keySet()
                                .stream()
                                .filter(object -> object.path().contains(current))
                                .findFirst();

                        if (currentNode.isPresent()) {
                            objects.get(currentNode.get())
                                    .add(new Node("." + split[split.length - 1], entry.type(), entry.numberType()));
                        } else {
                            Set<Node> set = new HashSet<>();
                            set.add(new Node("." + split[split.length - 1], entry.type(), entry.numberType()));

                            objects.put(new Node(current, JsonNodeType.OBJECT, null), set);
                        }

                    }
                }
            } else {
                objects.put(entry, null);
            }
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
                        list.add(new Node(format("%s%s[i]", currentNode, fieldName), JsonNodeType.STRING, null));
                    }
                    for (int i = 0; i < sourceValue.size(); i++) {
                        JsonNode current = sourceValue.get(i);
                        if (current.isObject() || current.isArray()) {
                            traverseAll(current, list, format("%s%s[i].", currentNode, fieldName));
                        } else {
                            JsonNodeType nodeType = current.getNodeType();

                            if (nodeType == JsonNodeType.NUMBER) {
                                list.add(new Node(
                                        format("%s%s[i]", currentNode, fieldName),
                                        current.getNodeType(),
                                        current.numberType()
                                ));
                            } else {
                                list.add(new Node(
                                        format("%s%s[i]", currentNode, fieldName),
                                        current.getNodeType(),
                                        null
                                ));
                            }

                            break;
                        }
                    }
                }
                default -> {
                    JsonNodeType nodeType = sourceValue.getNodeType();

                    if (nodeType == JsonNodeType.NUMBER) {
                        list.add(new Node(
                                format("%s%s", currentNode, fieldName),
                                sourceValue.getNodeType(),
                                sourceValue.numberType()
                        ));
                    } else {
                        list.add(new Node(
                                format("%s%s", currentNode, fieldName),
                                sourceValue.getNodeType(),
                                null
                        ));
                    }
                }
            }
        }

        return list;
    }
}
