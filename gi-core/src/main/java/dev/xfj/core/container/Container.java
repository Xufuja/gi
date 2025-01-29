package dev.xfj.core.container;

import dev.xfj.core.services.DatabaseService;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

public interface Container {
    Integer getId();
    String getName();
    String getDescription();
    Integer getRarity();
    
    default String getManualMappedText(String id) {
        return DatabaseService.getInstance().manualTextMapConfig
                .stream()
                .filter(text -> id.equals(text.getTextMapId()))
                .map(map -> DatabaseService.getInstance().getTranslation(map.getTextMapContentTextMapHash()))
                .findAny()
                .orElse(null);
    }

    default MaterialExcelConfigDataJson getItem(int id) {
        return DatabaseService.getInstance().materialConfig
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
