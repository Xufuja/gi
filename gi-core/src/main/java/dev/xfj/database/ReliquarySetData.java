package dev.xfj.database;

import dev.xfj.artifact.ArtifactSet;
import dev.xfj.artifact.ArtifactSetDetails;
import dev.xfj.jsonschema2pojo.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ReliquarySetData implements Data {
    private static ReliquarySetData instance;
    public final List<ReliquarySetExcelConfigDataJson> reliquarySetConfig;

    private ReliquarySetData() {
        try {
            this.reliquarySetConfig = loadJSONArray(ReliquarySetExcelConfigDataJson.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static ReliquarySetData getInstance() {
        if (instance == null) {
            instance = new ReliquarySetData();
        }

        return instance;
    }

    public Map<Integer, ArtifactSet> loadArtifactSets() {
        return loadDataWithIntegerId(ArtifactSet.class, reliquarySetConfig, "getSetId");
    }
}
