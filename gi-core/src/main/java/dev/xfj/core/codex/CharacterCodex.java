package dev.xfj.core.codex;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.xfj.core.services.DatabaseService;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CharacterCodex {
    private final AvatarCodexExcelConfigDataJson data;

    public CharacterCodex(AvatarCodexExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getAvatarId();
    }

    public String getName() {
        return DatabaseService.getInstance().avatarConfig
                .stream()
                .filter(character -> character.getId() == getId())
                .findFirst()
                .map(entry -> DatabaseService.getInstance().getTranslation(entry.getNameTextMapHash()))
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
