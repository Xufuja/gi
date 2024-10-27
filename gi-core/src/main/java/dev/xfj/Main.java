package dev.xfj;

import dev.xfj.database.Database;

public class Main {
    public static void main(String[] args) {
        try {
            Database.init();

            Database.getCharacters().forEach((key, value) -> {
                System.out.printf("Key: %s, Name Hash: %s (%s), Body Type: %s, Star: %s, Weapon: %s, ID: %s, Use: %s, Growth: %s\n",
                        key, Database.getTranslation(value.getNameHash()), value.getNameHash(), value.getBodyType(), value.getRarity(), value.getWeaponType(), value.getId(), value.getUsageType(), value.getStatGrowth());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}