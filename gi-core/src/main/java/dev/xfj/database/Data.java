package dev.xfj.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.xfj.constants.Global.DATA_PATH;

public interface Data {
    String EXCEL_BIN_PATH = DATA_PATH + "\\ExcelBinOutput\\";
    String TEXT_MAP_PATH = DATA_PATH + "\\TextMap\\";

    default <T> List<T> loadJSONArray(Class<T> clazz) throws FileNotFoundException {
        String file = clazz.getSimpleName().replace("Json", ".json");
        return loadJSONArray(clazz, EXCEL_BIN_PATH, file);
    }

    default <T> List<T> loadJSONArray(Class<T> clazz, String file) throws FileNotFoundException {
        return loadJSONArray(clazz, EXCEL_BIN_PATH, file);
    }

    default <T> List<T> loadJSONArray(Class<T> clazz, String baseDirectory, String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonArray jsonArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
        Type type = TypeToken.getParameterized(List.class, clazz).getType();

        System.out.printf("Loaded: %1$7d entries from %2$s%n", jsonArray.size(), file);

        return new Gson().fromJson(jsonArray, type);
    }

    default <T, U> Map<Integer, T> loadDataWithId(Class<T> returnType, List<U> inputList) {
        return inputList.stream()
                .collect(Collectors.toMap(this::getId, jsonData -> constructInstance(returnType, jsonData)));
    }

    default Integer getId(Object input) {
        try {
            Method method = input.getClass().getMethod("getId");
            return (Integer) method.invoke(input);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    default <T> T constructInstance(Class<T> returnType, Object argument) {
        try {
            Constructor<T> constructor = returnType.getConstructor(argument.getClass());
            return constructor.newInstance(argument);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}