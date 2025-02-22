package dev.xfj.core.services;

import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseWrapper {
    private static DatabaseService databaseService;

    @Autowired
    public DatabaseWrapper(DatabaseService databaseService) {
        DatabaseWrapper.databaseService = databaseService;
    }

    public static DatabaseService getDatabaseService() {
        return databaseService;
    }

    public static String getManualMappedText(String id) {
        return DatabaseWrapper.getDatabaseService().manualTextMapConfig
                .stream()
                .filter(text -> id.equals(text.getTextMapId()))
                .map(map -> DatabaseWrapper.getDatabaseService().getTranslation(map.getTextMapContentTextMapHash()))
                .findAny()
                .orElse(null);
    }

    public static MaterialExcelConfigDataJson getItem(int id) {
        return DatabaseWrapper.getDatabaseService().materialConfig
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
