package dev.xfj.codex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xfj.database.Database;
import dev.xfj.database.ReliquaryData;
import dev.xfj.jsonschema2pojo.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;

import java.util.Map;

public class ArtifactSetCodex {
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
        return ReliquaryData.getInstance().reliquaryConfig
                .stream()
                .filter(artifact -> artifact.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private String getName(ReliquaryExcelConfigDataJson artifact) {
        if (artifact != null) {
            return Database.getInstance().getTranslation(artifact.getNameTextMapHash());
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
