package dev.xfj.codex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.xfj.database.AvatarData;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CharacterCodex {
    private final AvatarCodexExcelConfigDataJson data;

    public CharacterCodex(AvatarCodexExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getAvatarId();
    }

    public String getName() {
        return AvatarData.getInstance().avatarConfig
                .stream()
                .filter(character -> character.getId() == getId())
                .findFirst()
                .map(entry -> Database.getInstance().getTranslation(entry.getNameTextMapHash()))
                .orElse("");
    }

    public LocalDateTime getReleaseTime() {
        return LocalDateTime.parse(data.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int getSortFactor() {
        return data.getSortFactor();
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", getId());
        jsonObject.addProperty("name", getName());
        jsonObject.addProperty("releaseTime", getReleaseTime().toString());
        jsonObject.addProperty("sortFactor", getSortFactor());

        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }
}
