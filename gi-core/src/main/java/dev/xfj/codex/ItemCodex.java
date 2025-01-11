package dev.xfj.codex;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;

public class ItemCodex {
    private final MaterialCodexExcelConfigDataJson data;

    public ItemCodex(MaterialCodexExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getMaterialId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
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
