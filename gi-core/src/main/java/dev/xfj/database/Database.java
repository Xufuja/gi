package dev.xfj.database;

import dev.xfj.character.Character;
import dev.xfj.weapon.Weapon;

import java.io.FileNotFoundException;
import java.util.Map;

public class Database {
    private static Map<Integer, Character> characters;
    private static Map<Integer, Weapon> weapons;

    private Database() {
    }

    public static void init() throws FileNotFoundException {
        TextMapData.init();
        characters = AvatarData.loadCharacters();
        weapons = WeaponData.loadWeapons();
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

    public static Map<Integer, Weapon> getWeapons() {
        return weapons;
    }
}
