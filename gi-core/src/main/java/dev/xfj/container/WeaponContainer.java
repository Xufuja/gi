package dev.xfj.container;

import dev.xfj.database.*;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponpromoteexcelconfigdata.CostItem;
import dev.xfj.jsonschema2pojo.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponcurveexcelconfigdata.CurveInfo;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponProp;
import dev.xfj.jsonschema2pojo.weaponpromoteexcelconfigdata.AddProp;
import dev.xfj.jsonschema2pojo.weaponpromoteexcelconfigdata.WeaponPromoteExcelConfigDataJson;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WeaponContainer implements Container, Ascendable {
    private static final String BASE_ATK = "FIGHT_PROP_BASE_ATTACK";
    private int id;
    private int currentLevel;
    private int currentExperience;
    private int currentAscension;
    private int currentRefinement;

    public WeaponContainer(int id) {
        this(id, 1, 0, 0, 1);
    }

    public WeaponContainer(int id, int currentLevel, int currentExperience, int currentAscension, int currentRefinement) {
        this.id = id;
        this.currentLevel = currentLevel;
        this.currentExperience = currentExperience;
        this.currentAscension = currentAscension;
        this.currentRefinement = currentRefinement;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return Database.getInstance().getTranslation(getWeapon().getNameTextMapHash());
    }

    public String getWeaponType() {
        return getManualMappedText(getWeapon().getWeaponType());
    }

    @Override
    public Integer getRarity() {
        return getWeapon().getRankLevel();
    }

    public double getBaseAttack() {
        return getBaseStat(getWeapon().getWeaponProp()
                        .stream()
                        .filter(curves -> curves.getPropType().equals(BASE_ATK))
                        .mapToDouble(WeaponProp::getInitValue)
                        .findFirst()
                        .orElse(-1.0),
                BASE_ATK);
    }

    @Override
    public Map<String, Double> getAscensionStat() {
        String ascensionStat = getWeapon().getWeaponProp()
                .stream()
                .map(WeaponProp::getPropType)
                .filter(propType -> !propType.equals(BASE_ATK))
                .findAny()
                .orElse("");

        return Map.of(
                getManualMappedText(ascensionStat),
                getBaseStat(
                        getWeapon().getWeaponProp()
                                .stream()
                                .filter(curves -> curves.getPropType().equals(ascensionStat))
                                .mapToDouble(WeaponProp::getInitValue)
                                .findFirst()
                                .orElse(-1.0),
                        ascensionStat
                ));
    }

    public String getEffect() {
        EquipAffixExcelConfigDataJson details = getRefinementDetails(currentRefinement);
        return String.format("%s\n%s", Database.getInstance().getTranslation(details.getNameTextMapHash()),
                Database.getInstance().getTranslation(details.getDescTextMapHash()));
    }

    @Override
    public String getDescription() {
        return Database.getInstance().getTranslation(getWeapon().getDescTextMapHash());
    }

    @Override
    public Map<Integer, Integer> getAscensionItems() {
        if (currentAscension < getMaxAscensions(getWeapon().getWeaponPromoteId())) {
            return getAscensionItems(currentAscension, currentAscension + 1);
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public Map<Integer, Integer> getAscensionItems(int startingAscension, int targetAscension) {
        return getAscensions(getWeapon().getWeaponPromoteId()).values()
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

    @Override
    public Integer getAscensionCost() {
        if (currentAscension < getMaxAscensions(getWeapon().getWeaponPromoteId())) {
            return getAscensionCost(currentAscension, currentAscension + 1);
        } else {
            return 0;
        }
    }

    @Override
    public Integer getAscensionCost(int startingAscension, int targetAscension) {
        return getAscensions(getWeapon().getWeaponPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() > startingAscension)
                .filter(ascension -> ascension.getPromoteLevel() <= targetAscension)
                .mapToInt(WeaponPromoteExcelConfigDataJson::getCoinCost)
                .sum();
    }

    @Override
    public Map<String, Integer> getAllAscensionItems() {
        return getAscensionItems(0, getMaxAscensions(getWeapon().getWeaponPromoteId())).entrySet()
                .stream()
                .filter(id -> id.getKey() != 0)
                .collect(Collectors.toMap(
                        item -> Database.getInstance().getTranslation(getItem(item.getKey()).getNameTextMapHash()),
                        Map.Entry::getValue
                ));
    }

    @Override
    public Integer getAllAscensionCosts() {
        return getAscensionCost(0, getMaxAscensions(getWeapon().getWeaponPromoteId()));
    }

    private double getBaseStat(double baseValue, String statType) {
        return (baseValue * getBaseStatMultiplier(statType)) + getExtraBaseStats(statType);
    }

    private double getExtraBaseStats(String selected) {
        return getAscensions(getWeapon().getWeaponPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() == currentAscension)
                .flatMap(promotions -> promotions.getAddProps().stream())
                .filter(prop -> prop.getPropType().equals(selected))
                .mapToDouble(AddProp::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getCurveMultiplier(String curveType) {
        return WeaponData.getInstance().weaponCurveConfig
                .stream()
                .filter(level -> level.getLevel() == currentLevel)
                .flatMap(curves -> curves.getCurveInfos().stream())
                .filter(curve -> curve.getType().equals(curveType))
                .map(CurveInfo::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getBaseStatMultiplier(String selected) {
        return getWeapon().getWeaponProp()
                .stream()
                .filter(curves -> curves.getPropType().equals(selected))
                .map(curve -> getCurveMultiplier(curve.getType()))
                .findFirst()
                .orElse(-1.0);
    }

    private WeaponExcelConfigDataJson getWeapon() {
        return WeaponData.getInstance().weaponConfig
                .stream()
                .filter(weapon -> weapon.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Map<Integer, WeaponPromoteExcelConfigDataJson> getAscensions(int promoteId) {
        return WeaponData.getInstance().weaponPromoteConfig
                .stream()
                .filter(ascension -> ascension.getWeaponPromoteId() == promoteId)
                .collect(Collectors.toMap(
                        WeaponPromoteExcelConfigDataJson::getPromoteLevel,
                        data -> data
                ));
    }

    private int getMaxAscensions(int promoteId) {
        return WeaponData.getInstance().weaponPromoteConfig
                .stream()
                .filter(ascension -> ascension.getWeaponPromoteId() == promoteId)
                .max(Comparator.comparing(WeaponPromoteExcelConfigDataJson::getPromoteLevel))
                .stream()
                .mapToInt(WeaponPromoteExcelConfigDataJson::getPromoteLevel)
                .findFirst()
                .orElse(-1);
    }

    private Map<Integer, EquipAffixExcelConfigDataJson> getAffixes(int affixId) {
        return ItemData.getInstance().equipAffixConfig
                .stream()
                .filter(affix -> affix.getId() == affixId)
                .collect(Collectors.toMap(
                        EquipAffixExcelConfigDataJson::getLevel,
                        affix -> affix
                ));
    }

    private EquipAffixExcelConfigDataJson getRefinementDetails(int refinement) {
        return getAffixes(getWeapon().getSkillAffix()
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
}
