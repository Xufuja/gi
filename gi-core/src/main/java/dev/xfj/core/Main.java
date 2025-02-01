package dev.xfj.core;

import dev.xfj.core.codex.Codex;
import dev.xfj.core.container.ArtifactContainer;
import dev.xfj.core.container.CharacterContainer;
import dev.xfj.core.container.WeaponContainer;
import dev.xfj.core.services.DatabaseService;

public class Main {
    public static void main(String[] args) {
        try {
            //CharacterContainer character = new CharacterContainer(10000094);
            CharacterContainer character = new CharacterContainer(10000107, 80, 0, 5);
            //Database.getInstance().setLanguage("JP");
            System.out.printf("ID: %s\nName: %s\nTitle: %s\nRarity: %s\n",
                    character.getId(), character.getName(), character.getTitle(), character.getRarity());
            System.out.println(character.getBaseHealth());
            System.out.println(character.getBaseAttack());
            System.out.println(character.getBaseDefense());
            System.out.println(character.getAscensionStat());
            System.out.println(character.getAscensionItems());
            System.out.println(character.getAscensionCost());
            System.out.println(character.getVision());
            System.out.println(character.getWeaponType());
            System.out.println(character.getConstellation());
            System.out.println(character.getNative());
            System.out.println(character.getBirthday());
            System.out.println(character.getVA("EN"));
            System.out.println(character.getVA("CHS"));
            System.out.println(character.getVA("JP"));
            System.out.println(character.getVA("KR"));
            System.out.println(character.getDescription());
            character.setCurrentTalentLevel(2, 15);
            System.out.println(character.getSkillDetails());
            System.out.println(character.getPassiveDetail());
            System.out.println(character.getConstellations());
            System.out.println(character.getAllAscensionItems());
            System.out.println(character.getAllAscensionCosts());
            System.out.println(character.getAllTalentItems());
            System.out.println(character.getAllTalentCosts());
            System.out.println(character.getAllExpBooks());
            System.out.println(character.getAllExpCosts());
            System.out.println(character.getAllItemRequirements());
            System.out.println(character.getAllItemCosts());
            System.out.println(character.getNameCardDescription());
            System.out.println(character.getSpecialtyFoodName());
            System.out.println(character.getOutfits());
            System.out.println(character.getTeaPotDetails());
            System.out.println(character.getStories());
            System.out.println(character.getQuotes());

            WeaponContainer weapon = new WeaponContainer(12514, 80, 0, 5, 5);
            //Database.getInstance().setLanguage("JP");
            System.out.printf("ID: %s\nName: %s\nTitle: %s\nRarity: %s\n",
                    weapon.getId(), weapon.getName(), weapon.getWeaponType(), weapon.getRarity());
            System.out.println(weapon.getBaseAttack());
            System.out.println(weapon.getAscensionStat());
            System.out.println(weapon.getEffect());
            System.out.println(weapon.getDescription());
            System.out.println(weapon.getAllAscensionItems());
            System.out.println(weapon.getAllAscensionCosts());
            System.out.println(weapon.getExpNeededForNextLevel());

            System.out.println(Codex.getArtifactSets());

            ArtifactContainer artifact = new ArtifactContainer(94543, 20, 0);
            System.out.printf("ID: %s\nSet Name: %s\nName: %s\nTitle: %s\nRarity: %s\n",
                    artifact.getId(), artifact.getSetName(), artifact.getName(), artifact.getArtifactType(), weapon.getRarity());

            System.out.println(artifact.getSetEffect());
            System.out.println(artifact.getDescription());
            System.out.println(artifact.getMainStats());
            System.out.println(artifact.getSubStats());
            System.out.println(artifact.getExpNeededForNextLevel());
            System.out.println(artifact.getArtifactsInSet());

            DatabaseService.getInstance().setLanguage("CHS");
            System.out.println(Codex.getItems());

            DatabaseService.getInstance().setLanguage("EN");
            System.out.println(Codex.getBooks());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}