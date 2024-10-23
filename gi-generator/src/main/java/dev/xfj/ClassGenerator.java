package dev.xfj;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
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

    public void createClasses() throws IOException {
        Set<String> set = getAllFiles();
        for (String item : set) {
            JsonReader jsonReader = new JsonReader(new FileReader(dataDirectory + item));
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            if (!jsonObject.entrySet().isEmpty()) {
                JsonArray array = findObject(jsonObject);
                Files.createDirectories(Paths.get(outputDirectory));
                createClass(array.toString(), new File(outputDirectory), "dev.xfj.jsonschema2pojo." + item.replace(".json", "").toLowerCase(), item);
            }
        }
    }

    private JsonArray findObject(JsonObject jsonObject) {
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            jsonArray.add(entry.getValue());

            boolean allNumeric = false;
            //Generally, when all keys are numeric, it looks like it should be an array with the name being an index
            for (var key : jsonArray.get(0).getAsJsonObject().keySet()) {
                allNumeric = key.chars().allMatch(Character::isDigit);
            }

            if (allNumeric) {
                jsonArray = findObject(jsonArray.get(0).getAsJsonObject());
            }
            //Only fetch the first entry
            break;
        }
        return jsonArray;
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

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new GsonAnnotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(jCodeModel, className, packageName, jsonString);

        jCodeModel.build(outputDirectory);
    }

    private Set<String> getAllFiles() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dataDirectory))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }
}
