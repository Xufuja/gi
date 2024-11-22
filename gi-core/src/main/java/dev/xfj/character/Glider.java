package dev.xfj.character;

import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.avatarflycloakexcelconfigdata.AvatarFlycloakExcelConfigDataJson;

public class Glider {
    private final AvatarFlycloakExcelConfigDataJson data;

    public Glider(AvatarFlycloakExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getFlycloakId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public int getItemId() {
        return data.getMaterialId();
    }

    public boolean isHidden() {
        return data.isHide();
    }

    public String getIconName() {
        return data.getIcon();
    }
}
