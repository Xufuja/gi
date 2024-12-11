package dev.xfj.database;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dev.xfj.constants.DataPath;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.xfj.constants.DataPath.EXCEL_BIN_OUTPUT;

interface Data {
    default <T> List<T> loadJSONArray(Class<T> clazz) throws FileNotFoundException {
        return loadJSONArray(EXCEL_BIN_OUTPUT, clazz);
    }

    default <T> List<T> loadJSONArray(DataPath dataPath, Class<T> clazz) throws FileNotFoundException {
        String file = clazz.getSimpleName().replace("Json", ".json");
        return loadJSONArray(clazz, dataPath.path, file);
    }

    default <T> List<T> loadJSONArray(DataPath dataPath, Class<T> clazz, String file) throws FileNotFoundException {
        return loadJSONArray(clazz, dataPath.path, file);
    }

    default <T> List<T> loadJSONArray(Class<T> clazz, String baseDirectory, String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonArray jsonArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
        Type type = TypeToken.getParameterized(List.class, clazz).getType();

        System.out.printf("Loaded: %1$7d entries from %2$s%n", jsonArray.size(), file);

        return new Gson().fromJson(jsonArray, type);
    }

    default <T> T loadJSON(DataPath dataPath, Class<T> clazz) throws FileNotFoundException {
        String file = clazz.getSimpleName().replace("Json", ".json");
        return loadJSON(clazz, dataPath.path, file);
    }

    default <T> T loadJSON(DataPath dataPath, Class<T> clazz, String file) throws FileNotFoundException {
        return loadJSON(clazz, dataPath.path, file);
    }

    default <T> T loadJSON(Class<T> clazz, String baseDirectory, String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
        //Type type = TypeToken.getParameterized(Map.class, String.class, clazz).getType();

        System.out.printf("Loaded: %1$7d entries from %2$s%n", jsonObject.keySet().size(), file);

        return new Gson().fromJson(jsonObject, clazz);
    }

    default <T, U> Map<Integer, T> loadDataWithIntegerId(Class<T> returnType, List<U> inputList) {
        return loadDataWithId(Integer.class, returnType, inputList);
    }

    default <T, U> Map<Integer, T> loadDataWithIntegerId(Class<T> returnType, List<U> inputList, String idMethod) {
        return loadDataWithId(Integer.class, returnType, inputList, idMethod);
    }

    default <T, U> Map<String, T> loadDataWithStringId(Class<T> returnType, List<U> inputList) {
        return loadDataWithId(String.class, returnType, inputList);
    }

    default <T, U> Map<String, T> loadDataWithStringId(Class<T> returnType, List<U> inputList, String idMethod) {
        return loadDataWithId(String.class, returnType, inputList, idMethod);
    }

    default <T, U, V> Map<T, U> loadDataWithId(Class<T> identifierType, Class<U> returnType, List<V> inputList) {
        return loadDataWithId(identifierType, returnType, inputList, "getId");
    }

    default <T, U, V> Map<T, U> loadDataWithId(Class<T> identifierType, Class<U> returnType, List<V> inputList, String idMethod) {
        return inputList.stream()
                .collect(Collectors.toMap(
                        input -> getId(identifierType, input, idMethod),
                        unwrappedData -> constructInstance(returnType, unwrappedData))
                );
    }

    default <T, U> Map<Integer, Map<Integer, T>> loadNestedDataWithIds(
            Class<T> returnType,
            List<U> inputList,
            String firstIdMethod,
            String secondIdMethod
    ) {
        return inputList
                .stream()
                .collect(Collectors.groupingBy(
                        input -> getId(Integer.class, input, firstIdMethod),
                        Collectors.mapping(
                                unwrappedData -> constructInstance(returnType, unwrappedData),
                                Collectors.toMap(wrappedData -> getId(Integer.class, wrappedData, secondIdMethod), data -> data)
                        )
                ));
    }

    default <T> T getId(Class<T> returnType, Object input, String idMethod) {
        try {
            Method method = input.getClass().getMethod(idMethod);
            return returnType.cast(method.invoke(input));
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
