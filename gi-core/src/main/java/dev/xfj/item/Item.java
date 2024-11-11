package dev.xfj.item;

import dev.xfj.constants.ItemType;
import dev.xfj.constants.SalvageType;
import dev.xfj.database.Database;
import dev.xfj.weapon.SalvageReturnItems;

import java.util.List;

public interface Item<T> {
    T data();

    default Integer getId() {
        return invoke(Integer.class, "getId");
    }

    default String getName() {
        return Database.getInstance().getTranslation(invoke(String.class, "getNameTextMapHash"));
    }

    default String getDescription() {
        return Database.getInstance().getTranslation(invoke(String.class, "getDescTextMapHash"));
    }

    default Integer getRarity() {
        return invoke(Integer.class, "getRankLevel");
    }

    default ItemType getItemType() {
        return ItemType.valueOf(invoke(String.class, "getItemType"));
    }

    default SalvageType getSalvageType() {
        String rule = invoke(String.class, "getDestroyRule");

        return rule != null ? SalvageType.valueOf(rule) : SalvageType.NONE;
    }

    default SalvageReturnItems getSalvagedItems() {
        List<Integer> count = invoke(List.class, "getDestroyReturnMaterialCount");
        //There is never more than 1 item in either array
        return count.size() > 0 ?
                new SalvageReturnItems(
                        (Integer) invoke(List.class, "getDestroyReturnMaterial").get(0),
                        count.get(0)
                ) :
                null;
    }

    default String getIconName() {
        return invoke(String.class, "getIcon");
    }

    default Integer getWeight() {
        return invoke(Integer.class, "getWeight");
    }

    default Integer getRank() {
        return invoke(Integer.class, "getRank");
    }

    default Integer getGadgetId() {
        return invoke(Integer.class, "getGadgetId");
    }

    default <R> R invoke(Class<R> returnType, String name) {
        Object data = data();

        try {
            return returnType.cast(data.getClass().getMethod(name).invoke(data));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
