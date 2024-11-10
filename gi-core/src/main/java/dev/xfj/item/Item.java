package dev.xfj.item;

import dev.xfj.constants.ItemType;
import dev.xfj.constants.SalvageType;
import dev.xfj.weapon.SalvageReturnItems;

public interface Item {
    int getId();

    String getName();

    String getDescription();

    int getRarity();

    ItemType getItemType();

    SalvageType getSalvageType();

    SalvageReturnItems getSalvagedItems();

    String getIconName();

    Integer getWeight();

    Integer getRank();

    Integer getGadgetId();
}
