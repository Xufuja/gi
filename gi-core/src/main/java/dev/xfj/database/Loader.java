package dev.xfj.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class Loader {
    public static <T> List<T> loadJSON(Class<T> clazz) throws FileNotFoundException {
        String file = clazz.getSimpleName().replace("Json", ".json");
        return loadJSON(clazz, "C:\\Dev\\AnimeGameData\\ExcelBinOutput\\", file);
    }

    public static <T> List<T> loadJSON(Class<T> clazz, String file) throws FileNotFoundException {
        return loadJSON(clazz, "C:\\Dev\\AnimeGameData\\ExcelBinOutput\\", file);
    }

    public static <T> List<T> loadJSON(Class<T> clazz, String baseDirectory, String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonArray jsonArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Type type = TypeToken.getParameterized(List.class, clazz).getType();

        List<T> result = gson.fromJson(jsonArray, type);
        System.out.printf("Loaded: %1$7d entries from %2$s%n", result.size(), file);
        return result;
    }
}
