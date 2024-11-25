package dev.xfj.artifact;

import dev.xfj.character.StatProperty;
import dev.xfj.constants.StatType;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class ArtifactSetDetails {
    private final EquipAffixExcelConfigDataJson data;

    public ArtifactSetDetails(EquipAffixExcelConfigDataJson data) {
        this.data = data;
    }

    public int getSetDetailsId() {
        return data.getId();
    }

    public int getSetBonusId() {
        return data.getAffixId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public String getSetBonus() {
        return data.getOpenConfig();
    }

    public List<StatProperty> getSetBonusStatGrowth() {
        return data.getAddProps()
                .stream()
                .map(stat ->
                        new StatProperty(StatType.valueOf(stat.getPropType()), stat.getValue()))
                .collect(Collectors.toList());
    }

    public List<Integer> getSetBonusParameters() {
        return data.getParamList();
    }
}
