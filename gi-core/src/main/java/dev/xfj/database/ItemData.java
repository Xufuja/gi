package dev.xfj.database;

import dev.xfj.jsonschema2pojo.bookscodexexcelconfigdata.BooksCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.cookbonusexcelconfigdata.CookBonusExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.furnituresuiteexcelconfigdata.FurnitureSuiteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldfurnitureexcelconfigdata.HomeWorldFurnitureExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldfurnituretypeexcelconfigdata.HomeWorldFurnitureTypeExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.rewardexcelconfigdata.RewardExcelConfigDataJson;

import java.util.List;

public class ItemData implements Data {
    private static ItemData instance;
    public final List<MaterialExcelConfigDataJson> materialConfig;
    public final List<RewardExcelConfigDataJson> rewardConfig;
    public final List<CookBonusExcelConfigDataJson> cookBonusConfig;
    public final List<FurnitureSuiteExcelConfigDataJson> furnitureSuiteConfig;
    public final List<HomeWorldFurnitureExcelConfigDataJson> homeWorldFurnitureConfig;
    public final List<HomeWorldFurnitureTypeExcelConfigDataJson> homeWorldFurnitureTypeConfig;
    public final List<EquipAffixExcelConfigDataJson> equipAffixConfig;
    public final List<MaterialCodexExcelConfigDataJson> materialCodexConfig;
    public final List<BooksCodexExcelConfigDataJson> booksCodexConfig;

    private ItemData() {
        try {
            this.materialConfig = loadJSONArray(MaterialExcelConfigDataJson.class);
            this.rewardConfig = loadJSONArray(RewardExcelConfigDataJson.class);
            this.cookBonusConfig = loadJSONArray(CookBonusExcelConfigDataJson.class);
            this.furnitureSuiteConfig = loadJSONArray(FurnitureSuiteExcelConfigDataJson.class);
            this.homeWorldFurnitureConfig = loadJSONArray(HomeWorldFurnitureExcelConfigDataJson.class);
            this.homeWorldFurnitureTypeConfig = loadJSONArray(HomeWorldFurnitureTypeExcelConfigDataJson.class);
            this.equipAffixConfig = loadJSONArray(EquipAffixExcelConfigDataJson.class);
            this.materialCodexConfig = loadJSONArray(MaterialCodexExcelConfigDataJson.class);
            this.booksCodexConfig = loadJSONArray(BooksCodexExcelConfigDataJson.class);
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
}
