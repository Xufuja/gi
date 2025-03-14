package dev.xfj.core.services;

import dev.xfj.core.dto.monster.MonsterCodexDTO;
import dev.xfj.generated.monsterexcelconfigdata.MonsterExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonsterService {
    private final DatabaseService databaseService;

    @Autowired
    public MonsterService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<MonsterCodexDTO> getMonsters() {
        return databaseService.monsterConfig
                .stream()
                .sorted(Comparator.comparing(MonsterExcelConfigDataJson::getId))
                .map(entry -> new MonsterCodexDTO(
                        entry.getId(),
                        databaseService.getTranslation(entry.getNameTextMapHash()),
                        entry.getId()
                ))
                .collect(Collectors.toList());
    }
}
