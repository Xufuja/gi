package dev.xfj.artifact;

import dev.xfj.constants.StatType;
import dev.xfj.jsonschema2pojo.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;

public class SubStat {
    private ReliquaryAffixExcelConfigDataJson data;

    public SubStat(ReliquaryAffixExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public int getSubStatTreeId() {
        return data.getDepotId();
    }

    public int getGroupId() {
        return data.getGroupId();
    }

    public StatType getStat() {
        return StatType.valueOf(data.getPropType());
    }

    public double getValue() {
        return data.getPropValue();
    }
}
