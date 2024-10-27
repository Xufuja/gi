package dev.xfj.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

import static dev.xfj.constants.Global.DATA_PATH;

public class Loader {
    private static final String EXCEL_BIN_PATH = DATA_PATH + "\\ExcelBinOutput\\";

    public static <T> List<T> loadJSONArray(Class<T> clazz) throws FileNotFoundException {
        String file = clazz.getSimpleName().replace("Json", ".json");
        return loadJSONArray(clazz, EXCEL_BIN_PATH, file);
    }

    public static <T> List<T> loadJSONArray(Class<T> clazz, String file) throws FileNotFoundException {
        return loadJSONArray(clazz, EXCEL_BIN_PATH, file);
    }

    public static <T> List<T> loadJSONArray(Class<T> clazz, String baseDirectory, String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonArray jsonArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
        Type type = TypeToken.getParameterized(List.class, clazz).getType();

        System.out.printf("Loaded: %1$7d entries from %2$s%n", jsonArray.size(), file);

        return  new Gson().fromJson(jsonArray, type);
    }
}
