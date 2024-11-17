package dev.xfj.artifact;

import dev.xfj.jsonschema2pojo.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;

import java.util.List;

public class ArtifactSet {
    private final ReliquarySetExcelConfigDataJson data;

    public ArtifactSet(ReliquarySetExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getSetId();
    }

    public int getSetDetailsId() {
        return data.getEquipAffixId();
    }

    public List<Integer> getSetBonusRequirements() {
        return data.getSetNeedNum();
    }

    public String getIconName() {
        return data.getSetIcon();
    }

}
