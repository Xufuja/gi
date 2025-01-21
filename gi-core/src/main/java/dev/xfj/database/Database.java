package dev.xfj.database;

import java.io.FileNotFoundException;

public class Database {
    private static Database instance;

    private Database() throws FileNotFoundException {
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
}
