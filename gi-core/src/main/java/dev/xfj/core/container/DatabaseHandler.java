package dev.xfj.core.container;

import dev.xfj.core.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class DatabaseHandler {
    private static DatabaseService databaseService;

    @Autowired
    public DatabaseHandler(DatabaseService databaseService) {
        DatabaseHandler.databaseService = databaseService;
    }

    public static DatabaseService getDatabaseService() {
        return databaseService;
    }
}
