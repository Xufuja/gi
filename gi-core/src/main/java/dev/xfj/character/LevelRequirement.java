package dev.xfj.character;

import dev.xfj.jsonschema2pojo.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;

public class LevelRequirement {
    private final AvatarLevelExcelConfigDataJson data;

    public LevelRequirement(AvatarLevelExcelConfigDataJson data) {
        this.data = data;
    }

    public int getLevel() {
        return data.getLevel();
    }

    public int getRequiredExp() {
        return data.getExp();
    }
}
