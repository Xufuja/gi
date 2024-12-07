package dev.xfj.database;

import dev.xfj.item.Material;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ItemData implements Data {
    private static ItemData instance;
    private final List<MaterialExcelConfigDataJson> materialConfig;

    private ItemData() throws FileNotFoundException {
        this.materialConfig = loadJSONArray(MaterialExcelConfigDataJson.class);
    }

    public static ItemData getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ItemData();
        }

        return instance;
    }

    public Map<Integer, Material> loadItems() {
        return loadDataWithIntegerId(Material.class, materialConfig);
    }
}
