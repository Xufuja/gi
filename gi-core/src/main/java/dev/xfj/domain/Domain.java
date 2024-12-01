package dev.xfj.domain;

import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.dungeonexcelconfigdata.DungeonExcelConfigDataJson;

public class Domain {
    private final DungeonExcelConfigDataJson data;

    public Domain(DungeonExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDisplayName() {
        return Database.getInstance().getTranslation(data.getDisplayNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }
}
