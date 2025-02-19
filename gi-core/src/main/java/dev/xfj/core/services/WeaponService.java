package dev.xfj.core.services;

import dev.xfj.core.dto.codex.WeaponCodexDTO;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeaponService {
    private final DatabaseService databaseService;

    @Autowired
    public WeaponService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<WeaponCodexDTO> getWeapons() {
        return databaseService.weaponConfig
                .stream()
                .sorted(Comparator.comparing(WeaponExcelConfigDataJson::getId))
                .map(entry -> new WeaponCodexDTO(
                        entry.getId(),
                        databaseService.getTranslation(entry.getNameTextMapHash()),
                        entry.getId()
                ))
                .collect(Collectors.toList());
    }
}
