package dev.xfj.character;

import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.avatartalentexcelconfigdata.AvatarTalentExcelConfigDataJson;

import java.util.List;

public class Constellation {
    private final AvatarTalentExcelConfigDataJson data;

    public Constellation(AvatarTalentExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getTalentId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public int getRequiredConstellationId() {
        return data.getPrevTalent();
    }

    public int getStellaFortunaId() {
        return data.getMainCostItemId();
    }

    public List<Double> getParameters() {
        return data.getParamList();
    }

    public String getIconName() {
        return data.getIcon();
    }
}
