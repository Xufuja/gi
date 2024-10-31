package dev.xfj.database;

import dev.xfj.character.Character;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AvatarData {
    private static AvatarData instance;
    private static List<AvatarExcelConfigDataJson> avatarConfig;

    private AvatarData() throws FileNotFoundException {
        avatarConfig = Loader.loadJSONArray(AvatarExcelConfigDataJson.class);
    }

    public static AvatarData getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new AvatarData();
        }

        return instance;
    }

    public Map<Integer, Character> loadCharacters() {
        return avatarConfig.stream().collect(Collectors.toMap(AvatarExcelConfigDataJson::getId, Character::new));
    }
}
