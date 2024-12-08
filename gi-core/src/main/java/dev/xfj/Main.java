package dev.xfj;

import dev.xfj.container.CharacterContainer;

public class Main {
    public static void main(String[] args) {
        try {
            /*Database.getInstance().getCharacters().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, Body Type: %s, Star: %s, Starting Weapon: %s, Weapon: %s, ID: %s, Use: %s, Growth: %s, Tree: %s\n",
                        key, value.getName(), value.getDescription(), value.getBodyType(), value.getRarity(), value.getStartingWeapon(), value.getWeaponType(), value.getId(), value.getUsageType(), value.getStatGrowth(), value.getDefaultTalentTreeId());
            });

            Database.getInstance().getWeapons().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, Growth: %s, Salvage: %s\n", value.getId(), value.getName(), value.getDescription(), value.getStatGrowth(), value.getSalvagedItems());
            });

            Database.getInstance().getTalents().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s\n", value.getId(), value.getName(), value.getDescription());
            });

            Database.getInstance().getTalentTrees().forEach((key, value) -> {
                System.out.printf("Key: %s, Arkhe: %s, Passives: %s\n", value.getId(), value.getArkhe(), value.getPassives());
            });

            Database.getInstance().getItems().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, ItemType: %s, Material: %s, Parameters: %s, Salvage Type: %s, Salvage: %s\n", value.getId(), value.getName(), value.getDescription(), value.getItemType(), value.getMaterialType(), value.getUsageParameters(), value.getSalvageType(), value.getSalvagedItems());
            });

            Database.getInstance().getArtifacts().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, Rarity: %s, Type: %s, Salvage: %s\n", value.getId(), value.getName(), value.getDescription(), value.getRarity(), value.getArtifactType(), value.getSalvagedItems());
            });

            Database.getInstance().getArtifactSets().forEach((key, value) -> {
                System.out.printf("Key: %s, Bonus: %s\n", value.getId(), value.getSetBonusRequirements());
            });

            Database.getInstance().getCharacterLevels().forEach((key, value) -> {
                System.out.printf("Level: %s, Required Exp: %s\n", value.getLevel(), value.getRequiredExp());
            });

            Database.getInstance().getCharacterAscensions().forEach((key, value) -> {
                Character selected = Database.getInstance().getCharacters().values().stream().filter(character -> character.getAscensionId() == key).findFirst().get();
                value.values().forEach(current -> System.out.printf("Character: %s, Ascension: %s, Max Level: %s, Stat Growth: %s\n", selected.getName(), current.getAscension(), current.getMaxLevel(), current.getAscensionStatGrowth()));
            });

            Database.getInstance().getWeaponAscensions().forEach((key, value) -> {
                Optional<Weapon> selected = Database.getInstance().getWeapons().values().stream().filter(weapon -> weapon.getAscensionId() == key).findFirst();
                if (selected.isPresent()) {
                    value.values().forEach(current -> System.out.printf("Weapon: %s (%s Star), Ascension: %s, Max Level: %s, Stat Growth: %s\n", selected.get().getName(), selected.get().getRarity(), current.getAscension(), current.getMaxLevel(), current.getAscensionStatGrowth()));
                } else {
                    value.values().forEach(current -> System.out.printf("Weapon: Not present in configuration for ascensionId: %s, Ascension: %s, Max Level: %s, Stat Growth: %s\n", key, current.getAscension(), current.getMaxLevel(), current.getAscensionStatGrowth()));
                }
            });

            Database.getInstance().getArtifactSetDetails().forEach((key, value) -> {
                Optional<ArtifactSet> selected = Database.getInstance().getArtifactSets().values().stream().filter(artifactSet -> artifactSet.getSetDetailsId() == key).findFirst();
                if (selected.isPresent()) {
                    value.values().forEach(current -> System.out.printf("Set ID: %s, Set Name: %s, Desc: %s\n", selected.get().getId(), current.getName(), current.getDescription()));
                } else {
                    value.values().forEach(current -> System.out.printf("Artifact Set: Not present in configuration for setDetailsId: %s, Set Bonus ID: %s\n", key, current.getSetBonusId()));
                }
            });

            Database.getInstance().getConstellations().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s\n", value.getId(), value.getName(), value.getDescription());
            });

            Database.getInstance().getInternalCooldowns().forEach((key, value) -> {
                System.out.printf("ID: %s, ICD: %s, Gauge: %s\n", value.getId(), value.getResetInterval(), value.getGaugeSequence());
            });

            Database.getInstance().getGliders().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, Hidden: %s\n", value.getId(), value.getName(), value.getDescription(), value.isHidden());
            });

            Database.getInstance().getMainStats().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Tree ID: %s, Stat: %s\n", value.getId(), value.getName(), value.getMainStatTreeId(), value.getStat());
            });

            Database.getInstance().getSubStats().forEach((key, value) -> {
                System.out.printf("Key: %s, Value: %s, Tree ID: %s, Stat: %s\n", value.getId(), value.getValue(), value.getSubStatTreeId(), value.getStat());
            });

            Database.getInstance().getArtifactLevels().forEach((key, value) -> {
                value.values().forEach(level -> System.out.printf("Rank: %s, Level: %s, Required: %s\n", key, level.getLevel(), level.getRequiredExp()));
            });

            Database.getInstance().getWeaponLevels().forEach((key, value) -> {
                value.values().forEach(level -> System.out.printf("Rank: %s, Level: %s, Required: %s\n", key, level.getLevel(), level.getRequiredExp()));
            });

            Database.getInstance().getSkins().forEach((key, value) -> {
                System.out.printf("Key: %s, Default: %s, Name: %s, Desc: %s\n", value.getId(), value.isDefaultSkin(), value.getName(), value.getDescription());
            });

            Database.getInstance().getLevelCurves().forEach((key, value) -> {
                System.out.printf("Level: %s, Curves: %s\n", value.getLevel(), value.getStatCurves());
            });

            Database.getInstance().getCharacterFriendshipLevels().forEach((key, value) -> {
                System.out.printf("Level: %s, Required Exp: %s\n", value.getLevel(), value.getRequiredExp());
            });

            Database.getInstance().getDomains().forEach((key, value) -> {
                System.out.printf("ID: %s, Display: %s, Name: %s, Desc: %s, Type: %s\n", value.getId(), value.getDisplayName(), value.getName(), value.getDescription(), value.getType());
            });

            Database.getInstance().getDomainEntries().forEach((key, value) -> {
                System.out.printf("ID: %s, Desc: %s, Type: %s, Default: %s, Conditions: %s\n", value.getId(), value.getDescription(), value.getType(), value.isAvailableByDefault(), value.getEntryConditions());
            });

            Database.getInstance().getManualTextMappings().forEach((key, value) -> {
                System.out.printf("ID: %s, Hash: %s, Parameters: %s\n", value.getId(), value.getHash(), value.getParameters());
            });*/

            //CharacterContainer character = new CharacterContainer(10000094);
            CharacterContainer character = new CharacterContainer(10000094, 80, 0, 5);
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
            var talents = character.getTalents();
            var passives = character.getPassives();
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}