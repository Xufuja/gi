package dev.xfj;

import dev.xfj.database.Database;

public class Main {
    public static void main(String[] args) {
        try {
            Database.getInstance().getCharacters().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, Body Type: %s, Star: %s, Starting Weapon: %s, Weapon: %s, ID: %s, Use: %s, Growth: %s\n",
                        key, value.getName(), value.getDescription(), value.getBodyType(), value.getRarity(), value.getStartingWeapon(), value.getWeaponType(), value.getId(), value.getUsageType(), value.getStatGrowth());
            });
            Database.getInstance().getWeapons().forEach((key, value) -> {
                System.out.printf("Key: %s, Name: %s, Desc: %s, Growth: %s, Salvage: %s\n", value.getId(), value.getName(), value.getDescription(), value.getStatGrowth(), value.getSalvagedItems());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}