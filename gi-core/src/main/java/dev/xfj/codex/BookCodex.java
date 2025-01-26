package dev.xfj.codex;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.xfj.services.Database;
import dev.xfj.jsonschema2pojo.bookscodexexcelconfigdata.BooksCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

public class BookCodex {
    private final BooksCodexExcelConfigDataJson data;

    public BookCodex(BooksCodexExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getMaterialId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(getItem().getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(getItem().getDescTextMapHash());
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

    private MaterialExcelConfigDataJson getItem() {
        return Database.getInstance().materialConfig
                .stream()
                .filter(item -> item.getId() == data.getMaterialId())
                .findFirst()
                .orElse(null);
    }
}
