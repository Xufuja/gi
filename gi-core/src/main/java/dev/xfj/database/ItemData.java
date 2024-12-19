package dev.xfj.database;

import dev.xfj.item.Material;
import dev.xfj.jsonschema2pojo.cookbonusexcelconfigdata.CookBonusExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.furnituresuiteexcelconfigdata.FurnitureSuiteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldfurnitureexcelconfigdata.HomeWorldFurnitureExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldfurnituretypeexcelconfigdata.HomeWorldFurnitureTypeExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldnpcexcelconfigdata.HomeWorldNPCExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.rewardexcelconfigdata.RewardExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ItemData implements Data {
    private static ItemData instance;
    public final List<MaterialExcelConfigDataJson> materialConfig;
    public final List<RewardExcelConfigDataJson> rewardConfig;
    public final List<CookBonusExcelConfigDataJson> cookBonusConfig;
    public final List<FurnitureSuiteExcelConfigDataJson> furnitureSuiteConfig;
    public final List<HomeWorldFurnitureExcelConfigDataJson> homeWorldFurnitureConfig;
    public final List<HomeWorldFurnitureTypeExcelConfigDataJson> homeWorldFurnitureTypeConfig;

    private ItemData() {
        try {
            this.materialConfig = loadJSONArray(MaterialExcelConfigDataJson.class);
            this.rewardConfig = loadJSONArray(RewardExcelConfigDataJson.class);
            this.cookBonusConfig = loadJSONArray(CookBonusExcelConfigDataJson.class);
            this.furnitureSuiteConfig = loadJSONArray(FurnitureSuiteExcelConfigDataJson.class);
            this.homeWorldFurnitureConfig = loadJSONArray(HomeWorldFurnitureExcelConfigDataJson.class);
            this.homeWorldFurnitureTypeConfig = loadJSONArray(HomeWorldFurnitureTypeExcelConfigDataJson.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static ItemData getInstance() {
        if (instance == null) {
            instance = new ItemData();
        }

        return instance;
    }

    public Map<Integer, Material> loadItems() {
        return loadDataWithIntegerId(Material.class, materialConfig);
    }
}
