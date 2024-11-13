package dev.xfj.database;

import dev.xfj.artifact.ArtifactSet;
import dev.xfj.jsonschema2pojo.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ReliquarySetData implements Data {
    private static ReliquarySetData instance;
    private final List<ReliquarySetExcelConfigDataJson> reliquarySetConfig;

    private ReliquarySetData() throws FileNotFoundException {
        this.reliquarySetConfig = loadJSONArray(ReliquarySetExcelConfigDataJson.class);
    }

    public static ReliquarySetData getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ReliquarySetData();
        }

        return instance;
    }

    public Map<Integer, ArtifactSet> loadArtifactSets() {
        return loadDataWithId(ArtifactSet.class, reliquarySetConfig, "getSetId");
    }
}
