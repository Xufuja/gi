package dev.xfj.container;

import dev.xfj.database.Database;
import dev.xfj.database.ItemData;
import dev.xfj.database.TextMapData;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

public interface Container {
    Integer getId();
    String getName();
    String getDescription();
    Integer getRarity();
    
    default String getManualMappedText(String id) {
        return TextMapData.getInstance().manualTextMapConfig
                .stream()
                .filter(text -> id.equals(text.getTextMapId()))
                .map(map -> Database.getInstance().getTranslation(map.getTextMapContentTextMapHash()))
                .findAny()
                .orElse(null);
    }

    default MaterialExcelConfigDataJson getItem(int id) {
        return ItemData.getInstance().materialConfig
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
