package dev.xfj.core.codex;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.xfj.core.services.DatabaseService;
import dev.xfj.jsonschema2pojo.bookscodexexcelconfigdata.BooksCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;

public class BookCodex {
    @Autowired
    private DatabaseService databaseService;
    private final BooksCodexExcelConfigDataJson data;

    public BookCodex(BooksCodexExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getMaterialId();
    }

    public String getName() {
        return databaseService.getTranslation(getItem().getNameTextMapHash());
    }

    public String getDescription() {
        return databaseService.getTranslation(getItem().getDescTextMapHash());
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
        return databaseService.materialConfig
                .stream()
                .filter(item -> item.getId() == data.getMaterialId())
                .findFirst()
                .orElse(null);
    }
}
