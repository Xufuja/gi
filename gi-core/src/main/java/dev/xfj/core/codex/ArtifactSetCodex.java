package dev.xfj.core.codex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xfj.core.services.DatabaseService;
import dev.xfj.jsonschema2pojo.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ArtifactSetCodex {
    @Autowired
    private DatabaseService databaseService;
    private final ReliquaryCodexExcelConfigDataJson data;

    public ArtifactSetCodex(ReliquaryCodexExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getSuitId();
    }

    public int getRarity() {
        return data.getLevel();
    }

    public Map<Integer, String> getGoblet() {
        return Map.of(
                data.getCupId(),
                getName(getArtifact(data.getCupId()))
        );
    }

    public Map<Integer, String> getFeather() {
        return Map.of(
                data.getLeatherId(),
                getName(getArtifact(data.getLeatherId()))
        );
    }

    public Map<Integer, String> getCirclet() {
        return Map.of(
                data.getCapId(),
                getName(getArtifact(data.getCapId()))
        );
    }

    public Map<Integer, String> getFlower() {
        return Map.of(
                data.getFlowerId(),
                getName(getArtifact(data.getFlowerId()))
        );
    }

    public Map<Integer, String> getSands() {
        return Map.of(
                data.getSandId(),
                getName(getArtifact(data.getSandId()))
        );
    }

    public int getSortFactor() {
        return data.getSortOrder();
    }

    private ReliquaryExcelConfigDataJson getArtifact(int id) {
        return databaseService.reliquaryConfig
                .stream()
                .filter(artifact -> artifact.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private String getName(ReliquaryExcelConfigDataJson artifact) {
        if (artifact != null) {
            return databaseService.getTranslation(artifact.getNameTextMapHash());
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", getId());
        jsonObject.addProperty("rarity", getRarity());
        jsonObject.add("goblet", fromMap(getGoblet()));
        jsonObject.add("feather", fromMap(getFeather()));
        jsonObject.add("circlet", fromMap(getCirclet()));
        jsonObject.add("flower", fromMap(getFlower()));
        jsonObject.add("sands", fromMap(getSands()));
        jsonObject.addProperty("sortFactor", getSortFactor());

        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }

    private JsonElement fromMap(Map<Integer, String> map) {
        return new Gson().toJsonTree(map);
    }
}
