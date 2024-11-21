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
    private final List<ReliquarySetExcelConfigDataJson> reliquarySetConfig;
    private final List<EquipAffixExcelConfigDataJson> equipAffixConfig;

    private ReliquarySetData() throws FileNotFoundException {
        this.reliquarySetConfig = loadJSONArray(ReliquarySetExcelConfigDataJson.class);
        this.equipAffixConfig = loadJSONArray(EquipAffixExcelConfigDataJson.class);
    }

    public static ReliquarySetData getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ReliquarySetData();
        }

        return instance;
    }

    public Map<Integer, ArtifactSet> loadArtifactSets() {
        return loadDataWithIntegerId(ArtifactSet.class, reliquarySetConfig, "getSetId");
    }

    public Map<Integer, Map<Integer, ArtifactSetDetails>> loadArtifactSetDetails() {
        return loadNestedDataWithIds(ArtifactSetDetails.class, equipAffixConfig, "getId", "getSetBonusId");
    }
}
