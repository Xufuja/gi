package dev.xfj.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;

public class TextMapData implements Data {
    private static TextMapData instance;
    private Map<String, String> languageMap;

    private TextMapData() throws FileNotFoundException {
        languageMap = loadLanguage("EN");
    }

    public static TextMapData getInstance() {
        if (instance == null) {
            try {
                instance = new TextMapData();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return instance;
    }

    protected void setLanguage(String language) {
        try {
            languageMap = loadLanguage(language);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private Map<String, String> loadLanguage(String language) throws FileNotFoundException {
        String file = String.format("TextMap%1$s.json", language);

        JsonReader jsonReader = new JsonReader(new FileReader(TEXT_MAP_PATH + file));
        JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
        Type type = TypeToken.getParameterized(Map.class, String.class, String.class).getType();

        Map<String, String> result = new Gson().fromJson(jsonObject, type);
        System.out.printf("Loaded: %1$7d entries from %2$s%n", result.keySet().size(), file);

        return result;
    }

    protected String getTranslation(String key) {
        return languageMap.get(key);
    }
}
