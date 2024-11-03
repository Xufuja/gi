package dev.xfj.character;

import dev.xfj.constants.ArkheType;
import dev.xfj.jsonschema2pojo.avatarskilldepotexcelconfigdata.AvatarSkillDepotExcelConfigDataJson;

public class TalentTree {
    private final AvatarSkillDepotExcelConfigDataJson data;

    public TalentTree(AvatarSkillDepotExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public ArkheType getArkhe() {
        return data.getLenbicofdeo() != null ? ArkheType.valueOf(data.getLenbicofdeo().toUpperCase()) : ArkheType.NONE;
    }
}
