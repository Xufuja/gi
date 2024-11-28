package dev.xfj.character;

import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.avatarcostumeexcelconfigdata.AvatarCostumeExcelConfigDataJson;

public class CharacterSkin {
    private final AvatarCostumeExcelConfigDataJson data;

    public CharacterSkin(AvatarCostumeExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getSkinId();
    }

    public int getIndexId() {
        return data.getIndexID();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public int getItemId() {
        return data.getItemId();
    }

    public int getCharacterId() {
        return data.getCharacterId();
    }

    public int getRarity() {
        return data.getQuality();
    }

    public boolean isAvailableByDefault() {
        return data.isIsDefaultUnlock();
    }

    public boolean isDefaultSkin() {
        return data.isIsDefault();
    }

    public String getIconName() {
        return data.getFrontIconName();
    }

    public String getSideViewIconName() {
        return data.getSideIconName();
    }
}
