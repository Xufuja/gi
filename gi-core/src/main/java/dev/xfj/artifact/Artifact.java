package dev.xfj.artifact;

import dev.xfj.constants.ArtifactType;
import dev.xfj.item.Item;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;

public record Artifact(ReliquaryExcelConfigDataJson data) implements Item<ReliquaryExcelConfigDataJson> {

    public ArtifactType getArtifactType() {
        return ArtifactType.valueOf(data.getEquipType());
    }
}
