package dev.xfj.database;

import dev.xfj.character.Character;
import dev.xfj.character.Talent;
import dev.xfj.character.TalentTree;
import dev.xfj.item.Material;
import dev.xfj.weapon.Weapon;

import java.io.FileNotFoundException;
import java.util.Map;

public class Database {
    private static Database instance;
    private final Map<Integer, Character> characters;
    private final Map<Integer, Weapon> weapons;
    private final Map<Integer, Talent> talents;
    private final Map<Integer, TalentTree> talentTrees;
    private final Map<Integer, Material> items;

    private Database() throws FileNotFoundException {
        characters = AvatarData.getInstance().loadCharacters();
        weapons = WeaponData.getInstance().loadWeapons();
        talents = AvatarData.getInstance().loadTalents();
        talentTrees = AvatarData.getInstance().loadTalentTrees();;
        items = ItemData.getInstance().loadItems();
    }

    public static Database getInstance() {
        if (instance == null) {
            try {
                instance = new Database();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return instance;
    }

    public void setLanguage(String language) {
        TextMapData.getInstance().setLanguage(language);
    }

    public  String getTranslation(String key) {
        return TextMapData.getInstance().getTranslation(key);
    }

    public Map<Integer, Character> getCharacters() {
        return characters;
    }

    public Map<Integer, Talent> getTalents() {
        return talents;
    }

    public Map<Integer, TalentTree> getTalentTrees() {
        return talentTrees;
    }

    public Map<Integer, Weapon> getWeapons() {
        return weapons;
    }

    public Weapon getWeapon(int id) {
        return weapons.get(id);
    }

    public Map<Integer, Material> getItems() {
        return items;
    }
}
