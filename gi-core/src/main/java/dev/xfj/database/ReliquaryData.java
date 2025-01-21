package dev.xfj.database;

import dev.xfj.jsonschema2pojo.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarylevelexcelconfigdata.ReliquaryLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;

import java.util.List;

public class ReliquaryData implements Data {
    private static ReliquaryData instance;
    public final List<ReliquaryExcelConfigDataJson> reliquaryConfig;
    public final List<ReliquaryMainPropExcelConfigDataJson> reliquaryMainPropConfig;
    public final List<ReliquaryAffixExcelConfigDataJson> reliquaryAffixConfig;
    public final List<ReliquaryLevelExcelConfigDataJson> reliquaryLevelConfig;
    public final List<ReliquaryCodexExcelConfigDataJson> reliquaryCodexConfig;

    private ReliquaryData() {
        try {
            this.reliquaryConfig = loadJSONArray(ReliquaryExcelConfigDataJson.class);
            this.reliquaryMainPropConfig = loadJSONArray(ReliquaryMainPropExcelConfigDataJson.class);
            this.reliquaryAffixConfig = loadJSONArray(ReliquaryAffixExcelConfigDataJson.class);
            this.reliquaryLevelConfig = loadJSONArray(ReliquaryLevelExcelConfigDataJson.class);
            this.reliquaryCodexConfig = loadJSONArray(ReliquaryCodexExcelConfigDataJson.class);
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
}
