package dev.xfj.codex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.xfj.database.AvatarData;
import dev.xfj.database.Database;
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
        return Database.getInstance().getTranslation(AvatarData.getInstance().avatarConfig
                .stream()
                .filter(character -> character.getId() == getId())
                .findFirst()
                .orElse(null).getNameTextMapHash()
        );
    }

    public LocalDateTime getReleaseTime() {
        return LocalDateTime.parse(data.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int getSortFactor() {
        return data.getSortFactor();
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", getId());
        jsonObject.addProperty("name", getName());
        jsonObject.addProperty("releaseTime", getReleaseTime().toString());
        jsonObject.addProperty("sortFactor", getSortFactor());

        return gson.toJson(jsonObject);
    }
}
