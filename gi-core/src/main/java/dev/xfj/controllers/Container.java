package dev.xfj.controllers;

import dev.xfj.services.Database;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

public interface Container {
    Integer getId();
    String getName();
    String getDescription();
    Integer getRarity();
    
    default String getManualMappedText(String id) {
        return Database.getInstance().manualTextMapConfig
                .stream()
                .filter(text -> id.equals(text.getTextMapId()))
                .map(map -> Database.getInstance().getTranslation(map.getTextMapContentTextMapHash()))
                .findAny()
                .orElse(null);
    }

    default MaterialExcelConfigDataJson getItem(int id) {
        return Database.getInstance().materialConfig
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
