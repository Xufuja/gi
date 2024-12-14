package dev.xfj.database;

import dev.xfj.item.Material;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ItemData implements Data {
    private static ItemData instance;
    public final List<MaterialExcelConfigDataJson> materialConfig;

    private ItemData() {
        try {
            this.materialConfig = loadJSONArray(MaterialExcelConfigDataJson.class);
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
