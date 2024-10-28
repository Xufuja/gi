package dev.xfj;

import dev.xfj.database.Database;

public class Main {
    public static void main(String[] args) {
        try {
            Database.init();

            Database.getCharacters().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Skills: %s, Body Type: %s, Star: %s, Weapon: %s, ID: %s, Use: %s, Growth: %s\n",
                        key, value.getName(), value.getDefaultSkillTreeId(), value.getBodyType(), value.getRarity(), value.getWeaponType(), value.getId(), value.getUsageType(), value.getStatGrowth());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}