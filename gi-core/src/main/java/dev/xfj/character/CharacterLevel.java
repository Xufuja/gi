package dev.xfj.character;

import dev.xfj.artifact.Level;
import dev.xfj.jsonschema2pojo.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;

public class CharacterLevel implements Level {
    private final AvatarLevelExcelConfigDataJson data;

    public CharacterLevel(AvatarLevelExcelConfigDataJson data) {
        this.data = data;
    }

    @Override
    public int getLevel() {
        return data.getLevel();
    }

    @Override
    public int getRequiredExp() {
        return data.getExp();
    }
}
