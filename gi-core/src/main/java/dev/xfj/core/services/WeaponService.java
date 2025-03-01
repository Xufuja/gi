package dev.xfj.core.services;

import dev.xfj.core.dto.character.MaterialsDTO;
import dev.xfj.core.dto.character.RequirementsDTO;
import dev.xfj.core.dto.codex.WeaponCodexDTO;
import dev.xfj.core.dto.weapon.WeaponProfileDTO;
import dev.xfj.core.logic.specification.WeaponSpecification;
import dev.xfj.core.utils.KeyValue;
import dev.xfj.generated.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.generated.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.generated.weaponcurveexcelconfigdata.CurveInfo;
import dev.xfj.generated.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.generated.weaponexcelconfigdata.WeaponProp;
import dev.xfj.generated.weaponlevelexcelconfigdata.WeaponLevelExcelConfigDataJson;
import dev.xfj.generated.weaponpromoteexcelconfigdata.AddProp;
import dev.xfj.generated.weaponpromoteexcelconfigdata.CostItem;
import dev.xfj.generated.weaponpromoteexcelconfigdata.WeaponPromoteExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

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

    public MaterialsDTO getMaterials(int weaponId) {
        WeaponSpecification weapon = new WeaponSpecification();
        weapon.id = weaponId;

        Map<String, Integer> idMap = getExpBooks().entrySet()
                .stream()
                .filter(entry -> databaseService.getWeapon(entry.getKey()) != null)
                .collect(Collectors.toMap(
                        entry -> databaseService.getTranslation(databaseService.getItemTextHash(entry.getKey()).key()),
                        entry -> databaseService.getWeapon(entry.getKey()).getRankLevel()
                ));

        return new MaterialsDTO(
                new RequirementsDTO(getAllExpBooks(weapon).entrySet()
                        .stream()
                        .map(entry -> !idMap.containsKey(entry.getKey()) ?
                                new KeyValue(entry.getKey(), entry.getValue()) :
                                new KeyValue(format("%s★ Weapon", idMap.get(entry.getKey())), entry.getValue()))
                        .collect(Collectors.toList()),
                        getAllExpCosts(weapon)),
                new RequirementsDTO(getAllAscensionItems(weapon).entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        getAllAscensionCosts(weapon)),
                null,
                new RequirementsDTO(getAllItemRequirements(weapon).entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        getAllItemCosts(weapon))
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
        return format("%s\n%s", databaseService.getTranslation(details.getNameTextMapHash()),
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

    private Map<String, Integer> getAllExpBooks(WeaponSpecification weaponSpecification) {
        Map<String, Integer> result = new LinkedHashMap<>();

        Map<Integer, WeaponPromoteExcelConfigDataJson> ascensions = getAscensions(getWeapon(weaponSpecification).getWeaponPromoteId());
        int nextStartingLevel = 1;

        for (int i = 0; i < ascensions.size(); i++) {
            WeaponPromoteExcelConfigDataJson current = ascensions.get(i);
            int currentMaxLevel = current.getUnlockMaxLevel();
            getExpBooksForLevel(weaponSpecification, nextStartingLevel, currentMaxLevel).forEach((id, count) -> result.merge(id, count, Integer::sum));
            nextStartingLevel = currentMaxLevel;
        }

        return result;
    }

    private Integer getAllExpCosts(WeaponSpecification weaponSpecification) {
        Map<String, Integer> expBooks = getExpBooks().keySet()
                .stream()
                .collect(Collectors.toMap(
                        book -> databaseService.getTranslation(databaseService.getItemTextHash(book).key()),
                        book -> book
                ));

        return getAllExpBooks(weaponSpecification).entrySet()
                .stream()
                .mapToInt(cost -> getCostForExpItem(expBooks.get(cost.getKey())) * cost.getValue())
                .sum();
    }

    private Map<String, Integer> getAllItemRequirements(WeaponSpecification weaponSpecification) {
        Map<String, Integer> result = getAllAscensionItems(weaponSpecification);
        getAllExpBooks(weaponSpecification).forEach((id, count) -> result.merge(id, count, Integer::sum));
        return result;
    }

    private Integer getAllItemCosts(WeaponSpecification characterSpecification) {
        return getAllAscensionCosts(characterSpecification) + getAllExpCosts(characterSpecification);
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

    private Map<String, Integer> getExpBooksForLevel(WeaponSpecification weaponSpecification, int startingLevel, int targetLevel) {
        int expRequired = getExpRequired(getRarity(weaponSpecification), startingLevel, targetLevel) - weaponSpecification.currentExperience;

        Map<Integer, Integer> expBooks = getExpBooks().entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (entryA, entryB) -> entryA,
                        LinkedHashMap::new));

        Map<Integer, Integer> count = new LinkedHashMap<>();
        expBooks.forEach((key, value) -> count.put(
                key,
                0
        ));
        int expRemaining = expRequired;

        Iterator<Map.Entry<Integer, Integer>> iterator = expBooks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();

            if (expRemaining <= 0) {
                break;
            }

            int expProvided = entry.getValue();
            double fractionalBooksNeeded = (double) expRemaining / expProvided;
            int booksNeeded = (int) fractionalBooksNeeded;

            if (!iterator.hasNext()) {
                booksNeeded++;
            }

            count.put(entry.getKey(), booksNeeded);
            expRemaining -= booksNeeded * expProvided;
        }

        if (expRemaining <= -200) {
            List<Integer> temp = count.entrySet()
                    .stream()
                    .map(entry -> entry.getKey())
                    .toList();

            for (int i = 0; i < temp.size(); i++) {
                if (i + 1 < temp.size() && expBooks.get(temp.get(i)) > 0 && expBooks.get(temp.get(i)) - expBooks.get(temp.get(i + 1)) == 200) {
                    count.put(temp.get(i), count.get(temp.get(i)) - 1);
                    count.put(temp.get(i + 1), count.get(temp.get(i + 1)) + 1);
                    expRemaining += 200;
                    break;
                }
            }
        }

        LinkedHashMap<String, Integer> result = count.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> databaseService.getTranslation(databaseService.getItemTextHash(entry.getKey()).key()),
                        entry -> entry.getValue(),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));

        checkExpBookEfficiency(result, expRemaining);
        return result;
    }

    private void checkExpBookEfficiency(Map<String, Integer> input, int expRemaining) {
        List<Integer> expBookIds = getExpBooks().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();

        for (int i = expBookIds.size() - 1; i > 0; i--) {
            String current = databaseService.getTranslation(databaseService.getItemTextHash(expBookIds.get(i)).key());
            String next = databaseService.getTranslation(databaseService.getItemTextHash(expBookIds.get(i - 1)).key());

            if (input.get(current) > 0 && expRemaining <= 0) {
                int currentExp = getExpBooks().get(expBookIds.get(i));
                int nextExp = getExpBooks().get(expBookIds.get(i - 1));
                int newRemaining = expRemaining + currentExp - nextExp;

                if (newRemaining > expRemaining && newRemaining <= 0) {
                    input.put(current, input.get(current) - 1);
                    input.put(next, input.get(next) + 1);
                    expRemaining = newRemaining;
                }
            }
        }

        for (int i = 0; i < expBookIds.size() - 1; i++) {
            String current = databaseService.getTranslation(databaseService.getItemTextHash(expBookIds.get(i)).key());
            String next = databaseService.getTranslation(databaseService.getItemTextHash(expBookIds.get(i + 1)).key());

            if (getCostForExpItem(expBookIds.get(i)) * input.get(current) >= getCostForExpItem(expBookIds.get(i + 1))) {
                input.put(current, 0);
                input.put(next, input.get(next) + 1);
            }
        }
    }

    private int getCostForExpItem(int id) {
        Map<Integer, Integer> expBooks = getExpBooks();
        expBooks.replaceAll((book, exp) -> exp / 10);

        return expBooks.entrySet()
                .stream()
                .filter(item -> item.getKey() == id)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private Map<Integer, Integer> getExpBooks() {
        Map<Integer, Integer> result = databaseService.materialConfig
                .stream()
                .filter(item -> "MATERIAL_WEAPON_EXP_STONE".equals(item.getMaterialType()))
                .collect(Collectors.toMap(
                        MaterialExcelConfigDataJson::getId,
                        item -> item.getItemUse()
                                .stream()
                                .filter(use -> "ITEM_USE_ADD_WEAPON_EXP".equals(use.getUseOp()))
                                .flatMap(use -> use.getUseParam().stream())
                                .filter(param -> !param.isBlank())
                                .mapToInt(Integer::parseInt)
                                .sum()
                ));

        Set<Integer> baseExp = new HashSet<>();

        databaseService.weaponConfig
                .stream()
                .filter(weapon -> weapon.getRankLevel() <= 3)
                .filter(present -> baseExp.add(present.getWeaponBaseExp()))
                .collect(Collectors.toMap(
                        WeaponExcelConfigDataJson::getId,
                        WeaponExcelConfigDataJson::getWeaponBaseExp,
                        (a, b) -> b
                ))
                .forEach((key, value) -> result.merge(key, value, (a, b) -> b));

        return result;
    }
}
