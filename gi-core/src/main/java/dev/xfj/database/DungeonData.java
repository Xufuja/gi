package dev.xfj.database;

import dev.xfj.domain.Domain;
import dev.xfj.jsonschema2pojo.dungeonexcelconfigdata.DungeonExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DungeonData implements Data {
    private static DungeonData instance;
    private final List<DungeonExcelConfigDataJson> dungeonConfig;

    private DungeonData() throws FileNotFoundException {
        this.dungeonConfig = loadJSONArray(DungeonExcelConfigDataJson.class);
    }

    public static DungeonData getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new DungeonData();
        }

        return instance;
    }

    public Map<Integer, Domain> loadDomains() {
        System.out.println(dungeonConfig.stream().map(DungeonExcelConfigDataJson::getMgggicjglhn).distinct().collect(Collectors.joining(",\n")));
        //System.out.println(dungeonConfig.stream().map(DungeonExcelConfigDataJson::getSettleShows).flatMap(List::stream).distinct().collect(Collectors.joining(",\n")));
        return loadDataWithIntegerId(Domain.class, dungeonConfig);
    }
}