package dev.xfj.database;

import dev.xfj.artifact.Artifact;
import dev.xfj.artifact.MainStat;
import dev.xfj.artifact.SubStat;
import dev.xfj.jsonschema2pojo.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ReliquaryData implements Data {
    private static ReliquaryData instance;
    private final List<ReliquaryExcelConfigDataJson> reliquaryConfig;
    private final List<ReliquaryMainPropExcelConfigDataJson> reliquaryMainPropConfig;
    private final List<ReliquaryAffixExcelConfigDataJson> reliquaryAffixConfigData;

    private ReliquaryData() throws FileNotFoundException {
        this.reliquaryConfig = loadJSONArray(ReliquaryExcelConfigDataJson.class);
        this.reliquaryMainPropConfig = loadJSONArray(ReliquaryMainPropExcelConfigDataJson.class);
        this.reliquaryAffixConfigData = loadJSONArray(ReliquaryAffixExcelConfigDataJson.class);
    }

    public static ReliquaryData getInstance() throws FileNotFoundException {
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
        return loadDataWithIntegerId(SubStat.class, reliquaryAffixConfigData);
    }
}
