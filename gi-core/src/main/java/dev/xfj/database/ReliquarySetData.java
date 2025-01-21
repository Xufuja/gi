package dev.xfj.database;

import dev.xfj.jsonschema2pojo.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;

import java.util.List;

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
}
