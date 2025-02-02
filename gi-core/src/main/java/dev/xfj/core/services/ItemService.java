package dev.xfj.core.services;

import dev.xfj.core.codex.ItemCodex;
import dev.xfj.core.codex.WeaponCodex;
import dev.xfj.core.dto.ItemCodexDTO;
import dev.xfj.core.dto.WeaponCodexDTO;
import dev.xfj.jsonschema2pojo.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    public List<ItemCodexDTO> getItems() {
        return DatabaseService.getInstance().materialCodexConfig
                .stream()
                .sorted(Comparator.comparing(MaterialCodexExcelConfigDataJson::getSortOrder))
                .map(ItemCodex::new)
                .toList()
                .stream()
                .map(entry -> new ItemCodexDTO(entry.getId(), entry.getName(), entry.getDescription(), entry.getSortFactor()))
                .collect(Collectors.toList());
    }
}
