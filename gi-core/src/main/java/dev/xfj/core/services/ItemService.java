package dev.xfj.core.services;

import dev.xfj.core.dto.codex.ItemCodexDTO;
import dev.xfj.generated.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final DatabaseService databaseService;

    @Autowired
    public ItemService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<ItemCodexDTO> getItems() {
        return databaseService.materialCodexConfig
                .stream()
                .sorted(Comparator.comparing(MaterialCodexExcelConfigDataJson::getSortOrder))
                .map(entry -> new ItemCodexDTO(
                        entry.getMaterialId(),
                        databaseService.getTranslation(entry.getNameTextMapHash()),
                        databaseService.getTranslation(entry.getDescTextMapHash()),
                        entry.getSortOrder()
                ))
                .collect(Collectors.toList());
    }
}
