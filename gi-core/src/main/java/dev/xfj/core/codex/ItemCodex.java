package dev.xfj.core.codex;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.xfj.core.services.DatabaseService;
import dev.xfj.jsonschema2pojo.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemCodex {
    @Autowired
    private DatabaseService databaseService;
    private final MaterialCodexExcelConfigDataJson data;

    public ItemCodex(MaterialCodexExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getMaterialId();
    }

    public String getName() {
        return databaseService.getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return databaseService.getTranslation(data.getDescTextMapHash());
    }

    public int getSortFactor() {
        return data.getSortOrder();
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", getId());
        jsonObject.addProperty("name", getName());
        jsonObject.addProperty("description", getDescription());
        jsonObject.addProperty("sortFactor", getSortFactor());

        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }
}
