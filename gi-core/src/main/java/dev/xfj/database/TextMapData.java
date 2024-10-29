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

import static dev.xfj.constants.Global.DATA_PATH;

public class TextMapData {
    private static Map<String, String> languageMap;

    private TextMapData() {
    }

    public static void init() throws FileNotFoundException {
        languageMap = loadLanguage("EN");
    }

    protected static void setLanguage(String language) throws FileNotFoundException {
        languageMap = loadLanguage(language);
    }

    private static Map<String, String> loadLanguage(String language) throws FileNotFoundException {
        String baseDirectory = DATA_PATH + "\\TextMap\\";
        String file = String.format("TextMap%1$s.json", language);

        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
        Type type = TypeToken.getParameterized(Map.class, String.class, String.class).getType();

        Map<String, String> result = new Gson().fromJson(jsonObject, type);
        System.out.printf("Loaded: %1$7d entries from %2$s%n", result.keySet().size(), file);

        return result;
    }

    protected static String getTranslation(String key) {
        return languageMap.get(key);
    }
}
