package dev.xfj.container;

import dev.xfj.database.*;
import dev.xfj.jsonschema2pojo.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;

import java.util.Map;
import java.util.stream.Collectors;

public class ArtifactContainer {
    private int id;
    private int currentLevel;
    private int currentExperience;

    public ArtifactContainer(int id) {
        this(id, 1, 0);
    }

    public ArtifactContainer(int id, int currentLevel, int currentExperience) {
        this.id = id;
        this.currentLevel = currentLevel;
        this.currentExperience = currentExperience;
    }

    public int getId() {
        return id;
    }

    public String getSetName() {
        return getAffixes(getArtifactSet().getEquipAffixId()).values()
                .stream()
                .findFirst()
                .map(entry -> Database.getInstance().getTranslation(entry.getNameTextMapHash()))
                .orElse(null);
    }

    public String getName() {
        return Database.getInstance().getTranslation(getArtifact().getNameTextMapHash());
    }

    public String getArtifactType() {
        return getManualMappedText(getArtifact().getEquipType());
    }

    public int getRarity() {
        return getArtifact().getRankLevel();
    }

    private ReliquaryExcelConfigDataJson getArtifact() {
        return ReliquaryData.getInstance().reliquaryConfig
                .stream()
                .filter(artifact -> artifact.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private String getManualMappedText(String id) {
        return TextMapData.getInstance().manualTextMapConfig
                .stream()
                .filter(text -> id.equals(text.getTextMapId()))
                .map(map -> Database.getInstance().getTranslation(map.getTextMapContentTextMapHash()))
                .findAny()
                .orElse(null);
    }

    private ReliquarySetExcelConfigDataJson getArtifactSet() {
        return ReliquarySetData.getInstance().reliquarySetConfig
                .stream()
                .filter(set -> set.getSetId() == getArtifact().getSetId())
                .findAny()
                .orElse(null);
    }

    private Map<Integer, EquipAffixExcelConfigDataJson> getAffixes(int affixId) {
        return ItemData.getInstance().equipAffixConfig
                .stream()
                .filter(affix -> affix.getId() == affixId)
                .collect(Collectors.toMap(
                        EquipAffixExcelConfigDataJson::getLevel,
                        affix -> affix
                ));
    }
}
