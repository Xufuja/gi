package dev.xfj.database;

import dev.xfj.domain.Domain;
import dev.xfj.domain.DomainEntry;
import dev.xfj.jsonschema2pojo.dungeonentryexcelconfigdata.DungeonEntryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.dungeonentryexcelconfigdata.SatisfiedCond;
import dev.xfj.jsonschema2pojo.dungeonexcelconfigdata.DungeonExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<Integer, Domain> loadDomains() {
        return loadDataWithIntegerId(Domain.class, dungeonConfig);
    }

    public Map<Integer, DomainEntry> loadDomainEntries() {
        return loadDataWithIntegerId(DomainEntry.class, dungeonEntryConfig);
    }
}