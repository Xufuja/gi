package dev.xfj;

import dev.xfj.database.Database;

public class Main {
    public static void main(String[] args) {
        try {
            Database.getInstance().getCharacters().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, Body Type: %s, Star: %s, Weapon: %s, ID: %s, Use: %s, Growth: %s\n",
                        key, value.getName(), value.getDescription(), value.getBodyType(), value.getRarity(), value.getWeaponType(), value.getId(), value.getUsageType(), value.getStatGrowth());
            });
            Database.getInstance().getWeapons().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, Growth: %s\n", value.getId(), value.getName(), value.getDescription(), value.getStatGrowth());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}