package dev.xfj;

import dev.xfj.database.Database;

public class Main {
    public static void main(String[] args) {
        try {
            Database.init();

            Database.getCharacters().forEach((key, value) -> {
                System.out.printf("Key: %s, Name Hash: %s (%s)\n", key, Database.getTranslation(value.getNameHash()), value.getNameHash());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}