package dev.xfj.core.services;

import dev.xfj.core.dto.monster.MonsterCodexDTO;
import dev.xfj.core.dto.monster.MonsterProfileDTO;
import dev.xfj.core.specification.MonsterSpecification;
import dev.xfj.generated.animalcodexexcelconfigdata.AnimalCodexExcelConfigDataJson;
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
        return databaseService.animalCodexConfig
                .stream()
                .sorted(Comparator.comparing(AnimalCodexExcelConfigDataJson::getSortOrder))
                .map(entry -> new MonsterCodexDTO(
                        entry.getId(),
                        getDescribe(entry.getDescribeId()) != null ?
                                databaseService.getTranslation(
                                        getDescribe(entry.getDescribeId()).getNameTextMapHash()
                                ) :
                                "",
                        entry.getSortOrder()
                ))
                .collect(Collectors.toList());
    }

    public MonsterProfileDTO getMonster(int monsterId) {
        MonsterSpecification monster = new MonsterSpecification();
        monster.id = monsterId;

        return new MonsterProfileDTO(
                monster.id,
                getName(monster),
                getSpecialName(monster),
                getDescription(monster)
        );
    }

    private String getName(MonsterSpecification monsterSpecification) {
        MonsterDescribeExcelConfigDataJson describe = getDescribe(getMonster(monsterSpecification).getDescribeId());

        return describe != null ?
                databaseService.getTranslation(
                        describe.getNameTextMapHash()
                ) :
                getMonster(monsterSpecification).getMonsterName();
    }

    private String getTitle(MonsterSpecification monsterSpecification) {
        return databaseService.monsterTitleConfig
                .stream()
                .filter(entry -> getDescribe(getMonster(monsterSpecification).getDescribeId())
                        .getTitleID() == entry.getTitleID())
                .map(entry -> databaseService.getTranslation(entry.getTitleNameTextMapHash()))
                .findFirst()
                .orElse("");
    }

    private String getSpecialName(MonsterSpecification monsterSpecification) {
        return databaseService.monsterSpecialNameConfig
                .stream()
                .filter(entry -> getDescribe(getMonster(monsterSpecification).getDescribeId())
                        .getSpecialNameLabID() == entry.getSpecialNameLabID())
                .map(entry -> databaseService.getTranslation(entry.getSpecialNameTextMapHash()))
                .findFirst()
                .orElse("");
    }

    private String getDescription(MonsterSpecification monsterSpecification) {
        return databaseService.animalCodexConfig
                .stream()
                .filter(entry -> entry.getId() == monsterSpecification.id)
                .map(entry -> databaseService.getTranslation(entry.getDescTextMapHash()))
                .findFirst()
                .orElse("");
    }

    private MonsterExcelConfigDataJson getMonster(MonsterSpecification monsterSpecification) {
        return databaseService.monsterConfig
                .stream()
                .filter(entry -> entry.getId() == monsterSpecification.id)
                .findFirst()
                .orElse(null);
    }

    private MonsterDescribeExcelConfigDataJson getDescribe(int id) {
        return databaseService.monsterDescribeConfig
                .stream()
                .filter(entry -> entry.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
