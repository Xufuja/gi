package dev.xfj.artifact;

import dev.xfj.constants.StatType;
import dev.xfj.jsonschema2pojo.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;

public class MainStat {
    private ReliquaryMainPropExcelConfigDataJson data;

    public MainStat(ReliquaryMainPropExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public String getName() {
        return data.getAffixName();
    }

    public int getMainStatTreeId() {
        return data.getPropDepotId();
    }

    public StatType getStat() {
        return StatType.valueOf(data.getPropType());
    }
}
