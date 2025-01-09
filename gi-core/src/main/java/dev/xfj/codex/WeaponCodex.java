package dev.xfj.codex;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public record WeaponCodex(int id, String name) {

    public int getSortFactor() {
        return id;
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id());
        jsonObject.addProperty("name", name());
        jsonObject.addProperty("sortFactor", getSortFactor());

        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }
}
