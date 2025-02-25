package dev.xfj.core.services;

import dev.xfj.core.dto.codex.WeaponCodexDTO;
import dev.xfj.core.dto.weapon.WeaponProfileDTO;
import dev.xfj.core.logic.specification.WeaponSpecification;
import dev.xfj.core.utils.KeyValue;
import dev.xfj.generated.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.generated.weaponcurveexcelconfigdata.CurveInfo;
import dev.xfj.generated.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.generated.weaponexcelconfigdata.WeaponProp;
import dev.xfj.generated.weaponlevelexcelconfigdata.WeaponLevelExcelConfigDataJson;
import dev.xfj.generated.weaponpromoteexcelconfigdata.AddProp;
import dev.xfj.generated.weaponpromoteexcelconfigdata.CostItem;
import dev.xfj.generated.weaponpromoteexcelconfigdata.WeaponPromoteExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeaponService {
    private final DatabaseService databaseService;
    private static final String BASE_ATK = "FIGHT_PROP_BASE_ATTACK";

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

    public WeaponProfileDTO getWeapon(int weaponId, int level, int experience, int ascension, int refinement) {
        WeaponSpecification weapon = new WeaponSpecification();
        weapon.id = weaponId;
        weapon.currentLevel = level;
        weapon.currentExperience = experience;
        weapon.currentAscension = ascension;
        weapon.currentRefinement = refinement;

        return new WeaponProfileDTO(
                weapon.id,
                getName(weapon),
                getWeaponType(weapon),
                getBaseAttack(weapon),
                getAscensionStat(weapon).entrySet().stream()
                        .findFirst()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .orElse(new KeyValue("", null)),
                getRarity(weapon),
                getEffect(weapon),
                getDescription(weapon)
        );
    }

    private String getName(WeaponSpecification weaponSpecification) {
        return databaseService.getTranslation(getWeapon(weaponSpecification).getNameTextMapHash());
    }

    private String getWeaponType(WeaponSpecification weaponSpecification) {
        return databaseService.getManualMappedText(getWeapon(weaponSpecification).getWeaponType());
    }

    private Integer getRarity(WeaponSpecification weaponSpecification) {
        return getWeapon(weaponSpecification).getRankLevel();
    }

    private double getBaseAttack(WeaponSpecification weaponSpecification) {
        return getBaseStat(weaponSpecification, getWeapon(weaponSpecification).getWeaponProp()
                        .stream()
                        .filter(curves -> curves.getPropType().equals(BASE_ATK))
                        .mapToDouble(WeaponProp::getInitValue)
                        .findFirst()
                        .orElse(-1.0),
                BASE_ATK);
    }

    private Map<String, Double> getAscensionStat(WeaponSpecification weaponSpecification) {
        String ascensionStat = getWeapon(weaponSpecification).getWeaponProp()
                .stream()
                .map(WeaponProp::getPropType)
                .filter(propType -> !propType.equals(BASE_ATK))
                .findAny()
                .orElse("");

        return Map.of(
                databaseService.getManualMappedText(ascensionStat),
                getBaseStat(
                        weaponSpecification,
                        getWeapon(weaponSpecification).getWeaponProp()
                                .stream()
                                .filter(curves -> curves.getPropType().equals(ascensionStat))
                                .mapToDouble(WeaponProp::getInitValue)
                                .findFirst()
                                .orElse(-1.0),
                        ascensionStat
                ));
    }

    private String getEffect(WeaponSpecification weaponSpecification) {
        EquipAffixExcelConfigDataJson details = getRefinementDetails(weaponSpecification, weaponSpecification.currentRefinement);
        return String.format("%s\n%s", databaseService.getTranslation(details.getNameTextMapHash()),
                databaseService.getTranslation(details.getDescTextMapHash()));
    }

    private String getDescription(WeaponSpecification weaponSpecification) {
        return databaseService.getTranslation(getWeapon(weaponSpecification).getDescTextMapHash());
    }

    private Map<Integer, Integer> getAscensionItems(WeaponSpecification weaponSpecification) {
        if (weaponSpecification.currentAscension < getMaxAscensions(getWeapon(weaponSpecification).getWeaponPromoteId())) {
            return getAscensionItems(weaponSpecification, weaponSpecification.currentAscension, weaponSpecification.currentAscension + 1);
        } else {
            return new HashMap<>();
        }
    }

    private Map<Integer, Integer> getAscensionItems(WeaponSpecification weaponSpecification, int startingAscension, int targetAscension) {
        return getAscensions(getWeapon(weaponSpecification).getWeaponPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() > startingAscension)
                .filter(ascension -> ascension.getPromoteLevel() <= targetAscension)
                .flatMap(promotions -> promotions.getCostItems().stream())
                .collect(Collectors.toMap(
                        CostItem::getId,
                        CostItem::getCount,
                        Integer::sum
                ));
    }

    private Integer getAscensionCost(WeaponSpecification weaponSpecification) {
        if (weaponSpecification.currentAscension < getMaxAscensions(getWeapon(weaponSpecification).getWeaponPromoteId())) {
            return getAscensionCost(weaponSpecification, weaponSpecification.currentAscension, weaponSpecification.currentAscension + 1);
        } else {
            return 0;
        }
    }

    private Integer getAscensionCost(WeaponSpecification weaponSpecification, int startingAscension, int targetAscension) {
        return getAscensions(getWeapon(weaponSpecification).getWeaponPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() > startingAscension)
                .filter(ascension -> ascension.getPromoteLevel() <= targetAscension)
                .mapToInt(WeaponPromoteExcelConfigDataJson::getCoinCost)
                .sum();
    }

    private Map<String, Integer> getAllAscensionItems(WeaponSpecification weaponSpecification) {
        return getAscensionItems(weaponSpecification, 0, getMaxAscensions(getWeapon(weaponSpecification).getWeaponPromoteId())).entrySet()
                .stream()
                .filter(id -> id.getKey() != 0)
                .collect(Collectors.toMap(
                        item -> databaseService.getTranslation(databaseService.getItem(item.getKey()).getNameTextMapHash()),
                        Map.Entry::getValue
                ));
    }

    private Integer getAllAscensionCosts(WeaponSpecification weaponSpecification) {
        return getAscensionCost(weaponSpecification, 0, getMaxAscensions(getWeapon(weaponSpecification).getWeaponPromoteId()));
    }

    private Integer getMaxLevel() {
        return databaseService.weaponLevelConfig
                .stream()
                .max(Comparator.comparing(WeaponLevelExcelConfigDataJson::getLevel))
                .stream()
                .mapToInt(WeaponLevelExcelConfigDataJson::getLevel)
                .findFirst()
                .orElse(-1);
    }

    private int getExpNeededForNextLevel(WeaponSpecification weaponSpecification) {
        if (weaponSpecification.currentLevel <= getMaxLevel()) {
            return getExpRequired(getRarity(weaponSpecification), weaponSpecification.currentLevel, weaponSpecification.currentLevel + 1) - weaponSpecification.currentExperience;
        }

        return 0;
    }

    private double getBaseStat(WeaponSpecification weaponSpecification, double baseValue, String statType) {
        return (baseValue * getBaseStatMultiplier(weaponSpecification, statType)) + getExtraBaseStats(weaponSpecification, statType);
    }

    private double getExtraBaseStats(WeaponSpecification weaponSpecification, String selected) {
        return getAscensions(getWeapon(weaponSpecification).getWeaponPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() == weaponSpecification.currentAscension)
                .flatMap(promotions -> promotions.getAddProps().stream())
                .filter(prop -> prop.getPropType().equals(selected))
                .mapToDouble(AddProp::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getCurveMultiplier(WeaponSpecification weaponSpecification, String curveType) {
        return databaseService.weaponCurveConfig
                .stream()
                .filter(level -> level.getLevel() == weaponSpecification.currentLevel)
                .flatMap(curves -> curves.getCurveInfos().stream())
                .filter(curve -> curve.getType().equals(curveType))
                .map(CurveInfo::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getBaseStatMultiplier(WeaponSpecification weaponSpecification, String selected) {
        return getWeapon(weaponSpecification).getWeaponProp()
                .stream()
                .filter(curves -> curves.getPropType().equals(selected))
                .map(curve -> getCurveMultiplier(weaponSpecification, curve.getType()))
                .findFirst()
                .orElse(-1.0);
    }

    private WeaponExcelConfigDataJson getWeapon(WeaponSpecification weaponSpecification) {
        return databaseService.weaponConfig
                .stream()
                .filter(weapon -> weapon.getId() == weaponSpecification.id)
                .findFirst()
                .orElse(null);
    }

    private Map<Integer, WeaponPromoteExcelConfigDataJson> getAscensions(int promoteId) {
        return databaseService.weaponPromoteConfig
                .stream()
                .filter(ascension -> ascension.getWeaponPromoteId() == promoteId)
                .collect(Collectors.toMap(
                        WeaponPromoteExcelConfigDataJson::getPromoteLevel,
                        data -> data
                ));
    }

    private int getMaxAscensions(int promoteId) {
        return databaseService.weaponPromoteConfig
                .stream()
                .filter(ascension -> ascension.getWeaponPromoteId() == promoteId)
                .max(Comparator.comparing(WeaponPromoteExcelConfigDataJson::getPromoteLevel))
                .stream()
                .mapToInt(WeaponPromoteExcelConfigDataJson::getPromoteLevel)
                .findFirst()
                .orElse(-1);
    }

    private Map<Integer, EquipAffixExcelConfigDataJson> getAffixes(int affixId) {
        return databaseService.equipAffixConfig
                .stream()
                .filter(affix -> affix.getId() == affixId)
                .collect(Collectors.toMap(
                        EquipAffixExcelConfigDataJson::getLevel,
                        affix -> affix
                ));
    }

    private EquipAffixExcelConfigDataJson getRefinementDetails(WeaponSpecification weaponSpecification, int refinement) {
        return getAffixes(getWeapon(weaponSpecification).getSkillAffix()
                .stream()
                .filter(affix -> affix != 0)
                .findFirst()
                .orElse(-1)).entrySet()
                .stream()
                .filter(entry -> entry.getKey() == refinement - 1)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private int getExpRequired(int rarity, int startingLevel, int targetLevel) {
        return databaseService.weaponLevelConfig
                .stream()
                .filter(level -> level.getLevel() >= startingLevel)
                .filter(level -> level.getLevel() < targetLevel)
                .mapToInt(entry -> entry.getRequiredExps().get(rarity - 1))
                .sum();
    }
}
