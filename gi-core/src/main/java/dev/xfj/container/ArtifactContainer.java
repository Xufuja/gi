package dev.xfj.container;

import dev.xfj.database.Database;
import dev.xfj.database.ReliquaryData;
import dev.xfj.database.TextMapData;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;

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
}
