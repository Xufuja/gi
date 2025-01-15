package dev.xfj.container;

import dev.xfj.database.Database;
import dev.xfj.database.ItemData;
import dev.xfj.database.TextMapData;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

import java.util.Map;

public interface Ascendable {
    Integer getId();
    String getName();
    String getDescription();
    Integer getRarity();
    Map<String, Double> getAscensionStat();
    Map<Integer, Integer> getAscensionItems();
    Map<Integer, Integer> getAscensionItems(int startingAscension, int targetAscension);
    Integer getAscensionCost();
    Integer getAscensionCost(int startingAscension, int targetAscension);
    Map<String, Integer> getAllAscensionItems();
    Integer getAllAscensionCosts();
}
