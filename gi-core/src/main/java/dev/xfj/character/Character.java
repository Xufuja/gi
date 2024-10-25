package dev.xfj.character;

import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;

public class Character {
    private final AvatarExcelConfigDataJson avatar;

    public Character(AvatarExcelConfigDataJson avatar) {
        this.avatar = avatar;
    }

    public long getNameHash() {
        return avatar.getNameTextMapHash();
    }
}
