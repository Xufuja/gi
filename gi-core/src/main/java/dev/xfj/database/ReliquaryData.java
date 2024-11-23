package dev.xfj.database;

import dev.xfj.artifact.Artifact;
import dev.xfj.artifact.MainStat;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ReliquaryData implements Data {
    private static ReliquaryData instance;
    private final List<ReliquaryExcelConfigDataJson> reliquaryConfig;
    private final List<ReliquaryMainPropExcelConfigDataJson> reliquaryMainPropConfig;

    private ReliquaryData() throws FileNotFoundException {
        this.reliquaryConfig = loadJSONArray(ReliquaryExcelConfigDataJson.class);
        this.reliquaryMainPropConfig = loadJSONArray(ReliquaryMainPropExcelConfigDataJson.class);
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
}
