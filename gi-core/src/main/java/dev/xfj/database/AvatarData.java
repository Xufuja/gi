package dev.xfj.database;

import dev.xfj.character.Character;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AvatarData {
    private static List<AvatarExcelConfigDataJson> avatarConfig;

    private AvatarData() {
    }

    public static void init() throws FileNotFoundException {
        avatarConfig = Loader.loadJSON(AvatarExcelConfigDataJson.class);
    }

    public static Map<Integer, Character> loadCharacters() {
        return avatarConfig.stream().collect(Collectors.toMap(AvatarExcelConfigDataJson::getId, Character::new));
    }
}
