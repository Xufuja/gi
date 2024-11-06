package dev.xfj.item;

import dev.xfj.constants.ItemType;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

public class Item {
    private final MaterialExcelConfigDataJson data;

    public Item(MaterialExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public ItemType getItemType() {
        return ItemType.valueOf(data.getItemType());
    }
}
