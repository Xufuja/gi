package dev.xfj.container;

import dev.xfj.database.Database;
import dev.xfj.database.TextMapData;

public interface Container {
    int getId();
    String getName();
    String getDescription();

    default String getManualMappedText(String id) {
        return TextMapData.getInstance().manualTextMapConfig
                .stream()
                .filter(text -> id.equals(text.getTextMapId()))
                .map(map -> Database.getInstance().getTranslation(map.getTextMapContentTextMapHash()))
                .findAny()
                .orElse(null);
    }
}
