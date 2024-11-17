package dev.xfj.character;

import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AvatarPromoteExcelConfigDataJson;

public record CharacterAscension(
        AvatarPromoteExcelConfigDataJson data) implements Ascension<AvatarPromoteExcelConfigDataJson> {

    @Override
    public int getAscensionId() {
        return data.getAvatarPromoteId();
    }

    @Override
    public int getAscensionCost() {
        return data.getScoinCost();
    }
}
