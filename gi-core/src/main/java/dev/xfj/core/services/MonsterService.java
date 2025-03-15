package dev.xfj.core.services;

import dev.xfj.core.dto.monster.MonsterCodexDTO;
import dev.xfj.generated.monsterdescribeexcelconfigdata.MonsterDescribeExcelConfigDataJson;
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
                        getDescribe(entry.getDescribeId()) != null ?
                                databaseService.getTranslation(
                                        getDescribe(entry.getDescribeId()).getNameTextMapHash()
                                ) :
                                entry.getMonsterName(),
                        entry.getId()
                ))
                .collect(Collectors.toList());
    }

    private MonsterDescribeExcelConfigDataJson getDescribe(int id) {
        return databaseService.monsterDescribeConfig
                .stream()
                .filter(entry -> entry.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
