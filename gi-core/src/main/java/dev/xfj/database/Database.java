package dev.xfj.database;

import dev.xfj.character.Character;

import java.io.FileNotFoundException;
import java.util.Map;

public class Database {
    private static Map<Integer, Character> characters;

    private Database() {
    }

    public static void init() throws FileNotFoundException {
        TextMapData.init();
        characters = AvatarData.loadCharacters();
    }

    public static void setLanguage(String language) throws FileNotFoundException {
        TextMapData.setLanguage(language);
    }

    public static String getTranslation(String key) {
        return TextMapData.getTranslation(key);
    }

    public static Map<Integer, Character> getCharacters() {
        return characters;
    }
}
