package dev.xfj.artifact;

import dev.xfj.character.StatProperty;
import dev.xfj.constants.StatType;
import dev.xfj.jsonschema2pojo.reliquarylevelexcelconfigdata.ReliquaryLevelExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class ArtifactLevel {
    private final ReliquaryLevelExcelConfigDataJson data;

    public ArtifactLevel(ReliquaryLevelExcelConfigDataJson data) {
        this.data = data;
    }

    public int getRank() {
        return data.getRank();
    }

    public int getLevel() {
        return data.getLevel();
    }

    public int getRequiredExp() {
        return data.getExp();
    }

    public List<StatProperty> getStatGrowth() {
        return data.getAddProps()
                .stream()
                .map(stat ->
                        new StatProperty(StatType.valueOf(stat.getPropType()), stat.getValue()))
                .collect(Collectors.toList());
    }
}
