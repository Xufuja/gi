package dev.xfj.database;

import dev.xfj.character.Character;

import java.io.FileNotFoundException;
import java.util.Map;

public class Database {
    private static Map<Integer, Character> characters;

    private Database() {
    }

    public static void init() throws FileNotFoundException {
        AvatarData.init();
        characters = AvatarData.loadCharacters();
    }

    public static Map<Integer, Character> getCharacters() {
        return characters;
    }
}
