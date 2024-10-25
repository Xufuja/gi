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

public class TextMapData {
    private static Map<Long, String> languageMap;

    private TextMapData() {
    }

    public static void init() throws FileNotFoundException {
        languageMap = loadLanguage("EN");
    }

    protected static void setLanguage(String language) throws FileNotFoundException {
        languageMap = loadLanguage(language);
    }

    private static Map<Long, String> loadLanguage(String language) throws FileNotFoundException {
        String baseDirectory = "C:\\Dev\\AnimeGameData\\TextMap\\";
        String file = String.format("TextMap%1$s.json", language);

        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
        Type type = TypeToken.getParameterized(Map.class, Long.class, String.class).getType();

        Map<Long, String> result = new Gson().fromJson(jsonObject, type);
        System.out.printf("Loaded: %1$7d entries from %2$s%n", result.keySet().size(), file);

        return result;
    }

    protected static String getTranslation(long key) {
        return languageMap.get(key);
    }
}
