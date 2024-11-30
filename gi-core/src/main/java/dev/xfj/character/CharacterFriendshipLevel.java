package dev.xfj.character;

import dev.xfj.artifact.Level;
import dev.xfj.jsonschema2pojo.avatarfetterslevelexcelconfigdata.AvatarFettersLevelExcelConfigDataJson;

public class CharacterFriendshipLevel implements Level {
    private final AvatarFettersLevelExcelConfigDataJson data;

    public CharacterFriendshipLevel(AvatarFettersLevelExcelConfigDataJson data) {
        this.data = data;
    }

    @Override
    public int getLevel() {
        return data.getFetterLevel();
    }

    @Override
    public int getRequiredExp() {
        return data.getNeedExp();
    }
}
