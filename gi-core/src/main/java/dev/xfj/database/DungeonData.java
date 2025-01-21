package dev.xfj.database;

import dev.xfj.jsonschema2pojo.dungeonentryexcelconfigdata.DungeonEntryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.dungeonexcelconfigdata.DungeonExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;

public class DungeonData implements Data {
    private static DungeonData instance;
    private final List<DungeonExcelConfigDataJson> dungeonConfig;
    private final List<DungeonEntryExcelConfigDataJson> dungeonEntryConfig;

    private DungeonData() throws FileNotFoundException {
        this.dungeonConfig = loadJSONArray(DungeonExcelConfigDataJson.class);
        this.dungeonEntryConfig = loadJSONArray(DungeonEntryExcelConfigDataJson.class);
    }

    public static DungeonData getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new DungeonData();
        }

        return instance;
    }
}