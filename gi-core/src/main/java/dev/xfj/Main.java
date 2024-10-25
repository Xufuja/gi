package dev.xfj;

import dev.xfj.database.Database;

public class Main {
    public static void main(String[] args) {
        try {
            Database.init();

            Database.getCharacters().forEach((key, value) -> {
                System.out.printf("Key: %s, Name Hash: %s%n", key, value.getNameHash());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}