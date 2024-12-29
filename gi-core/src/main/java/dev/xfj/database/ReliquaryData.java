package dev.xfj.database;

import dev.xfj.artifact.Artifact;
import dev.xfj.artifact.ArtifactLevel;
import dev.xfj.artifact.MainStat;
import dev.xfj.artifact.SubStat;
import dev.xfj.jsonschema2pojo.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarylevelexcelconfigdata.ReliquaryLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ReliquaryData implements Data {
    private static ReliquaryData instance;
    public final List<ReliquaryExcelConfigDataJson> reliquaryConfig;
    private final List<ReliquaryMainPropExcelConfigDataJson> reliquaryMainPropConfig;
    private final List<ReliquaryAffixExcelConfigDataJson> reliquaryAffixConfig;
    private final List<ReliquaryLevelExcelConfigDataJson> reliquaryLevelConfig;

    private ReliquaryData() {
        try {
            this.reliquaryConfig = loadJSONArray(ReliquaryExcelConfigDataJson.class);
            this.reliquaryMainPropConfig = loadJSONArray(ReliquaryMainPropExcelConfigDataJson.class);
            this.reliquaryAffixConfig = loadJSONArray(ReliquaryAffixExcelConfigDataJson.class);
            this.reliquaryLevelConfig = loadJSONArray(ReliquaryLevelExcelConfigDataJson.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static ReliquaryData getInstance() {
        if (instance == null) {
            instance = new ReliquaryData();
        }

        return instance;
    }

    public Map<Integer, Artifact> loadArtifacts() {
        return loadDataWithIntegerId(Artifact.class, reliquaryConfig);
    }

    public Map<Integer, MainStat> loadMainStats() {
        return loadDataWithIntegerId(MainStat.class, reliquaryMainPropConfig);
    }

    public Map<Integer, SubStat> loadSubStats() {
        return loadDataWithIntegerId(SubStat.class, reliquaryAffixConfig);
    }

    public Map<Integer, Map<Integer, ArtifactLevel>> loadLevelRequirements() {
        return loadNestedDataWithIds(
                ArtifactLevel.class,
                reliquaryLevelConfig,
                "getRank",
                "getLevel");
    }
}
