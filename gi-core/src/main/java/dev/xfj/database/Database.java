package dev.xfj.database;

import dev.xfj.artifact.*;
import dev.xfj.character.*;
import dev.xfj.character.Character;
import dev.xfj.domain.Domain;
import dev.xfj.domain.DomainEntry;
import dev.xfj.item.Material;
import dev.xfj.weapon.Weapon;
import dev.xfj.weapon.WeaponAscension;
import dev.xfj.weapon.WeaponLevel;

import java.io.FileNotFoundException;
import java.util.Map;

public class Database {
    private static Database instance;
    private final Map<Integer, Character> characters;
    private final Map<Integer, Weapon> weapons;
    private final Map<Integer, Talent> talents;
    private final Map<Integer, TalentTree> talentTrees;
    private final Map<Integer, Material> items;
    private final Map<Integer, Artifact> artifacts;
    private final Map<Integer, ArtifactSet> artifactSets;
    private final Map<Integer, CharacterLevel> characterLevels;
    private final Map<Integer, Map<Integer, CharacterAscension>> characterAscensions;
    private final Map<Integer, Map<Integer, WeaponAscension>> weaponAscensions;
    private final Map<Integer, Map<Integer, ArtifactSetDetails>> artifactSetDetails;
    private final Map<Integer, Constellation> constellations;
    private final Map<String, InternalCooldown> internalCooldowns;
    private final Map<Integer, Glider> gliders;
    private final Map<Integer, MainStat> mainStats;
    private final Map<Integer, SubStat> subStats;
    private final Map<Integer, Map<Integer, ArtifactLevel>> artifactLevels;
    private final Map<Integer, Map<Integer, WeaponLevel>> weaponLevels;
    private final Map<Integer, CharacterSkin> skins;
    private final Map<Integer, CharacterLevelCurve> levelCurves;
    private final Map<Integer, CharacterFriendshipLevel> characterFriendshipLevels;
    private final Map<Integer, Domain> domains;
    private final Map<Integer, DomainEntry> domainEntries;

    private Database() throws FileNotFoundException {
        characters = AvatarData.getInstance().loadCharacters();
        weapons = WeaponData.getInstance().loadWeapons();
        talents = AvatarData.getInstance().loadTalents();
        talentTrees = AvatarData.getInstance().loadTalentTrees();;
        items = ItemData.getInstance().loadItems();
        artifacts = ReliquaryData.getInstance().loadArtifacts();
        artifactSets = ReliquarySetData.getInstance().loadArtifactSets();
        characterLevels = AvatarData.getInstance().loadLevelRequirements();
        characterAscensions = AvatarData.getInstance().loadCharacterAscensions();
        weaponAscensions = WeaponData.getInstance().loadWeaponAscensions();
        artifactSetDetails = ReliquarySetData.getInstance().loadArtifactSetDetails();
        constellations = AvatarData.getInstance().loadConstellations();
        internalCooldowns = AvatarData.getInstance().loadInternalCooldown();
        gliders = AvatarData.getInstance().loadGliders();
        mainStats = ReliquaryData.getInstance().loadMainStats();
        subStats = ReliquaryData.getInstance().loadSubStats();
        artifactLevels = ReliquaryData.getInstance().loadLevelRequirements();
        weaponLevels = WeaponData.getInstance().loadLevelRequirements();
        skins = AvatarData.getInstance().loadSkins();
        levelCurves = AvatarData.getInstance().loadLevelCurves();
        characterFriendshipLevels = AvatarData.getInstance().loadFriendshipLevelRequirements();
        domains = DungeonData.getInstance().loadDomains();
        domainEntries = DungeonData.getInstance().loadDomainEntries();
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

    public Map<Integer, Artifact> getArtifacts() {
        return artifacts;
    }

    public Map<Integer, ArtifactSet> getArtifactSets() {
        return artifactSets;
    }

    public Map<Integer, CharacterLevel> getCharacterLevels() {
        return characterLevels;
    }

    public Map<Integer, Map<Integer, CharacterAscension>> getCharacterAscensions() {
        return characterAscensions;
    }

    public Map<Integer, Map<Integer, WeaponAscension>> getWeaponAscensions() {
        return weaponAscensions;
    }

    public Map<Integer, Map<Integer, ArtifactSetDetails>> getArtifactSetDetails() {
        return artifactSetDetails;
    }

    public Map<Integer, Constellation> getConstellations() {
        return constellations;
    }

    public Map<String, InternalCooldown> getInternalCooldowns() {
        return internalCooldowns;
    }

    public Map<Integer, Glider> getGliders() {
        return gliders;
    }

    public Map<Integer, MainStat> getMainStats() {
        return mainStats;
    }

    public Map<Integer, SubStat> getSubStats() {
        return subStats;
    }

    public Map<Integer, Map<Integer, ArtifactLevel>> getArtifactLevels() {
        return artifactLevels;
    }

    public Map<Integer, Map<Integer, WeaponLevel>> getWeaponLevels() {
        return weaponLevels;
    }

    public Map<Integer, CharacterSkin> getSkins() {
        return skins;
    }

    public Map<Integer, CharacterLevelCurve> getLevelCurves() {
        return levelCurves;
    }

    public Map<Integer, CharacterFriendshipLevel> getCharacterFriendshipLevels() {
        return characterFriendshipLevels;
    }

    public Map<Integer, Domain> getDomains() {
        return domains;
    }

    public Map<Integer, DomainEntry> getDomainEntries() {
        return domainEntries;
    }
}
