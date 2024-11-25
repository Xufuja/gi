package dev.xfj.character;

import dev.xfj.jsonschema2pojo.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;

public class CharacterLevel {
    private final AvatarLevelExcelConfigDataJson data;

    public CharacterLevel(AvatarLevelExcelConfigDataJson data) {
        this.data = data;
    }

    public int getLevel() {
        return data.getLevel();
    }

    public int getRequiredExp() {
        return data.getExp();
    }
}
