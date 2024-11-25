package dev.xfj.character;

import dev.xfj.constants.StatType;
import dev.xfj.item.ItemPair;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public interface Ascension<T> {
    T data();

    int getAscensionId();

    default Integer getAscension() {
        return invoke(Integer.class, "getPromoteLevel");
    }

    int getAscensionCost();

    default List<ItemPair> getAscensionItems() {
        return (List<ItemPair>) invoke(List.class, "getCostItems")
                .stream()
                .map(item ->
                        new ItemPair(getInteger(item, "getId"), getInteger(item, "getCount")))
                .collect(Collectors.toList());
    }

    default int getMaxLevel() {
        return invoke(Integer.class, "getUnlockMaxLevel");
    }

    default List<StatProperty> getAscensionStatGrowth() {
        return (List<StatProperty>) invoke(List.class, "getAddProps")
                .stream()
                .map(stat ->
                        new StatProperty(StatType.valueOf(getObject(String.class, stat, "getPropType")), getObject(Double.class, stat, "getValue")))
                .collect(Collectors.toList());
    }

    default Integer getRequiredAdventureRank() {
        return invoke(Integer.class, "getRequiredPlayerLevel");
    }

    default <R> R invoke(Class<R> returnType, String name) {
        Object data = data();

        try {
            return returnType.cast(data.getClass().getMethod(name).invoke(data));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    default Integer getInteger(Object input, String integerMethod) {
        return getObject(Integer.class, input, integerMethod);
    }

    default <R> R getObject(Class<R> returnType, Object input, String methodName) {
        try {
            Method method = input.getClass().getMethod(methodName);
            return returnType.cast(method.invoke(input));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
