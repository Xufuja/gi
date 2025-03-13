package dev.xfj.core.services;

import dev.xfj.core.dto.item.ItemCodexDTO;
import dev.xfj.core.dto.item.ItemProfileDTO;
import dev.xfj.core.specification.ItemSpecification;
import dev.xfj.generated.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;
import dev.xfj.generated.materialexcelconfigdata.MaterialExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public ItemProfileDTO getItem(int itemId) {
        ItemSpecification item = new ItemSpecification();
        item.id = itemId;

        return new ItemProfileDTO(
                item.id,
                getName(item),
                getItemType(item),
                getRarity(item),
                getDescription(item),
                getItemSources(item)
        );
    }

    private String getName(ItemSpecification itemSpecification) {
        return databaseService.getTranslation(getItem(itemSpecification).getNameTextMapHash());
    }

    private String getItemType(ItemSpecification itemSpecification) {
        return databaseService.getTranslation(getItem(itemSpecification).getTypeDescTextMapHash());
    }

    private Integer getRarity(ItemSpecification itemSpecification) {
        return getItem(itemSpecification).getRankLevel();
    }

    private String getDescription(ItemSpecification itemSpecification) {
        return databaseService.getTranslation(getItem(itemSpecification).getDescTextMapHash());
    }

    private MaterialExcelConfigDataJson getItem(ItemSpecification itemSpecification) {
        return databaseService.materialConfig
                .stream()
                .filter(item -> item.getId() == itemSpecification.id)
                .findFirst()
                .orElse(null);
    }

    private List<String> getItemSources(ItemSpecification itemSpecification) {
        return databaseService.materialSourceDataConfig
                .stream()
                .filter(item -> item.getId() == itemSpecification.id)
                .flatMap(source -> Stream.concat(source.getJumpDescs().stream(), source.getTextList().stream()))
                .map(databaseService::getTranslation)
                .filter(source -> source != null && !source.isEmpty())
                .collect(Collectors.toList());

    }
}
