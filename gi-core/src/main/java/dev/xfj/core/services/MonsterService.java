package dev.xfj.core.services;

import dev.xfj.core.dto.monster.MonsterCodexDTO;
import dev.xfj.core.dto.monster.MonsterProfileDTO;
import dev.xfj.core.dto.monster.MonsterStatsDTO;
import dev.xfj.core.specification.MonsterSpecification;
import dev.xfj.generated.animalcodexexcelconfigdata.AnimalCodexExcelConfigDataJson;
import dev.xfj.generated.monstercurveexcelconfigdata.CurveInfo;
import dev.xfj.generated.monstercurveexcelconfigdata.MonsterCurveExcelConfigDataJson;
import dev.xfj.generated.monsterdescribeexcelconfigdata.MonsterDescribeExcelConfigDataJson;
import dev.xfj.generated.monsterexcelconfigdata.MonsterExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MonsterService {
    private final DatabaseService databaseService;
    private static final String BASE_HP = "FIGHT_PROP_BASE_HP";
    private static final String BASE_DEF = "FIGHT_PROP_BASE_DEFENSE";
    private static final String BASE_ATK = "FIGHT_PROP_BASE_ATTACK";

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

    public List<MonsterStatsDTO> getStats(int monsterId) {
        MonsterSpecification monster = new MonsterSpecification();
        monster.id = monsterId;

        return IntStream.range(1, getMaxLevel() + 1)
                .boxed()
                .map(i -> {
                            monster.currentLevel = i;
                            return new MonsterStatsDTO(
                                    i,
                                    getBaseHealth(monster),
                                    getMultiplayerHealth(monster, getBaseHealth(monster), 0),
                                    getMultiplayerHealth(monster, getBaseHealth(monster), 1),
                                    getMultiplayerHealth(monster, getBaseHealth(monster), 2),
                                    getBaseAttack(monster),
                                    getMultiplayerAttack(monster, getBaseAttack(monster), 0),
                                    getMultiplayerAttack(monster, getBaseAttack(monster), 1),
                                    getMultiplayerAttack(monster, getBaseAttack(monster), 2),
                                    getBaseDefense(monster)
                            );
                        }
                )
                .collect(Collectors.toList());
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

    private double getBaseHealth(MonsterSpecification monsterSpecification) {
        return getBaseStat(monsterSpecification, getMonster(monsterSpecification).getHpBase(), BASE_HP);
    }

    private double getBaseAttack(MonsterSpecification monsterSpecification) {
        return getBaseStat(monsterSpecification, getMonster(monsterSpecification).getAttackBase(), BASE_ATK);
    }

    private double getBaseDefense(MonsterSpecification monsterSpecification) {
        return getBaseStat(monsterSpecification, getMonster(monsterSpecification).getDefenseBase(), BASE_DEF);
    }

    private double getBaseStat(MonsterSpecification monsterSpecification, double baseValue, String statType) {
        return (baseValue * getBaseStatMultiplier(monsterSpecification, statType));
    }

    private double getExtraBaseStats(MonsterSpecification monsterSpecification, String selected, int index) {
        return databaseService.monsterMultiPlayerConfig
                .stream()
                .filter(entry -> entry.getId() == getMonster(monsterSpecification).getMpPropID())
                .flatMap(entry -> entry.getPropPer().stream())
                .filter(prop -> prop.getPropType().equals(selected))
                .mapToDouble(prop -> prop.getPropValue().get(index))
                .findFirst()
                .orElse(-1.0);
    }

    private double getCurveMultiplier(MonsterSpecification monsterSpecification, String curveType) {
        return databaseService.monsterCurveConfig
                .stream()
                .filter(level -> level.getLevel() == monsterSpecification.currentLevel)
                .flatMap(curves -> curves.getCurveInfos().stream())
                .filter(curve -> curve.getType().equals(curveType))
                .map(CurveInfo::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getBaseStatMultiplier(MonsterSpecification monsterSpecification, String selected) {
        return getMonster(monsterSpecification).getPropGrowCurves()
                .stream()
                .filter(curves -> curves.getType().equals(selected))
                .map(curve -> getCurveMultiplier(monsterSpecification, curve.getGrowCurve()))
                .findFirst()
                .orElse(-1.0);
    }

    private Integer getMaxLevel() {
        return databaseService.monsterCurveConfig
                .stream()
                .max(Comparator.comparing(MonsterCurveExcelConfigDataJson::getLevel))
                .stream()
                .mapToInt(MonsterCurveExcelConfigDataJson::getLevel)
                .findFirst()
                .orElse(-1);
    }

    private double getMultiplayerHealth(MonsterSpecification monsterSpecification, double health, int index) {
        return health + health * getExtraBaseStats(monsterSpecification, "FIGHT_PROP_HP_MP_PERCENT", index);
    }

    private double getMultiplayerAttack(MonsterSpecification monsterSpecification, double attack, int index) {
        return attack + attack * getExtraBaseStats(monsterSpecification, "FIGHT_PROP_ATTACK_MP_PERCENT", index);
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
