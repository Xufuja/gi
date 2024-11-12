package dev.xfj.artifact;

import dev.xfj.constants.ArtifactType;
import dev.xfj.item.Item;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public record Artifact(ReliquaryExcelConfigDataJson data) implements Item<ReliquaryExcelConfigDataJson> {

    public ArtifactType getArtifactType() {
        return ArtifactType.valueOf(data.getEquipType());
    }

    public int getMainStatTreeId() {
        return data.getMainPropDepotId();
    }

    public int getSubStatTreeId() {
        return data.getAppendPropDepotId();
    }

    public int getMaxSubStatCount() {
        return data.getAppendPropNum();
    }

    public int getSetId() {
        return data.getSetId();
    }

    public List<Integer> getSubStatLevels() {
        return data.getAddPropLevels().stream().map(integer -> integer - 1).collect(Collectors.toList());
    }

    public int getMaxLevel() {
        return data.getMaxLevel();
    }

    public int getExpValue() {
        return data.getBaseConvExp();
    }
}
