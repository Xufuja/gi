package dev.xfj.container;

import dev.xfj.constants.CharacterRarity;
import dev.xfj.database.AvatarData;
import dev.xfj.database.Database;
import dev.xfj.database.ItemData;
import dev.xfj.jsonschema2pojo.avatarcurveexcelconfigdata.CurveInfo;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AddProp;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AvatarPromoteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.CostItem;
import dev.xfj.jsonschema2pojo.avatarskilldepotexcelconfigdata.AvatarSkillDepotExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarskillexcelconfigdata.AvatarSkillExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatartalentexcelconfigdata.AvatarTalentExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fetterinfoexcelconfigdata.FetterInfoExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.proudskillexcelconfigdata.ProudSkillExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.trainingguideexpcostconfigdata.TrainingGuideExpCostConfigDataJson;
import dev.xfj.utils.Interpolator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;

public class CharacterContainer {
    private static final String BASE_HP = "FIGHT_PROP_BASE_HP";
    private static final String BASE_DEF = "FIGHT_PROP_BASE_DEFENSE";
    private static final String BASE_ATK = "FIGHT_PROP_BASE_ATTACK";
    private int id;
    private int currentLevel;
    private int currentExperience;
    private int currentAscension;
    private Map<Integer, Integer> currentTalentLevels;

    public CharacterContainer(int id) {
        this(id, 1, 0, 0);
    }

    public CharacterContainer(int id, int currentLevel, int currentExperience, int currentAscension) {
        this.id = id;
        this.currentLevel = currentLevel;
        this.currentExperience = currentExperience;
        this.currentAscension = currentAscension;
        this.currentTalentLevels = getTalents().keySet()
                .stream()
                .map(AvatarSkillExcelConfigDataJson::getId)
                .collect(Collectors.toMap(
                        skillId -> skillId,
                        level -> 1
                ));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Database.getInstance().getTranslation(getAvatar().getNameTextMapHash());
    }

    public String getTitle() {
        return Database.getInstance().getTranslation(getFetter().getAvatarTitleTextMapHash());
    }

    public int getRarity() {
        return CharacterRarity.valueOf(getAvatar().getQualityType()).getStarValue();
    }

    public double getBaseHealth() {
        return getBaseStat(getAvatar().getHpBase(), BASE_HP);
    }

    public double getBaseAttack() {
        return getBaseStat(getAvatar().getAttackBase(), BASE_ATK);
    }

    public double getBaseDefense() {
        return getBaseStat(getAvatar().getDefenseBase(), BASE_DEF);
    }

    public double getBaseCritRate() {
        return getAvatar().getCritical();
    }

    public double getBaseCritDamage() {
        return getAvatar().getCriticalHurt();
    }

    public Map<String, Double> getAscensionStat() {
        return getAscensions(getAvatar().getAvatarPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() == currentAscension)
                .flatMap(promotions -> promotions.getAddProps().stream())
                .filter(prop ->
                        !prop.getPropType().equals(BASE_HP) &&
                                !prop.getPropType().equals(BASE_DEF) &&
                                !prop.getPropType().equals(BASE_ATK)
                )
                .collect(Collectors.toMap(
                        AddProp::getPropType,
                        AddProp::getValue
                ));
    }

    public Map<Integer, Integer> getAscensionItems() {
        return getAscensionItems(false);
    }

    public Map<Integer, Integer> getAscensionItems(boolean allAscensions) {
        return getAscensions(getAvatar().getAvatarPromoteId()).values()
                .stream()
                .filter(ascension -> !allAscensions ?
                        ascension.getPromoteLevel() == currentAscension :
                        ascension.getPromoteLevel() <= 6)
                .flatMap(promotions -> promotions.getCostItems().stream())
                .collect(Collectors.toMap(
                        CostItem::getId,
                        CostItem::getCount,
                        Integer::sum
                ));
    }

    public Integer getAscensionCost() {
        return getAscensionCost(false);
    }

    public Integer getAscensionCost(boolean allAscensions) {
        return getAscensions(getAvatar().getAvatarPromoteId()).values()
                .stream()
                .filter(ascension -> !allAscensions ?
                        ascension.getPromoteLevel() == currentAscension :
                        ascension.getPromoteLevel() <= 6)
                .mapToInt(AvatarPromoteExcelConfigDataJson::getScoinCost)
                .sum();
    }

    public String getVision() {
        return Database.getInstance().getTranslation(getFetter().getAvatarVisionBeforTextMapHash());
    }

    public String getWeaponType() {
        return getAvatar().getWeaponType();
    }

    public String getConstellation() {
        return Database.getInstance().getTranslation(getFetter().getAvatarConstellationBeforTextMapHash());
    }

    public String getNative() {
        return Database.getInstance().getTranslation(getFetter().getAvatarNativeTextMapHash());
    }

    public String getBirthday() {
        return format("%1$2s/%2$2s",
                getFetter().getInfoBirthMonth(), getFetter().getInfoBirthDay()).replace(' ', '0');
    }

    public String getVA(String language) {
        return switch (language) {
            case "EN" -> Database.getInstance().getTranslation(getFetter().getCvEnglishTextMapHash());
            case "JP" -> Database.getInstance().getTranslation(getFetter().getCvJapaneseTextMapHash());
            case "CHS" -> Database.getInstance().getTranslation(getFetter().getCvChineseTextMapHash());
            case "KR" -> Database.getInstance().getTranslation(getFetter().getCvKoreanTextMapHash());
            default -> "No VA available";
        };
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(getFetter().getAvatarDetailTextMapHash());
    }

    public Map<AvatarSkillExcelConfigDataJson, Map<Integer, ProudSkillExcelConfigDataJson>> getTalents() {
        Map<AvatarSkillExcelConfigDataJson, Map<Integer, ProudSkillExcelConfigDataJson>> talents =
                getSkillDepot().getSkills()
                        .stream()
                        .filter(id -> id != 0)
                        .map(this::getSkill)
                        .collect(Collectors.toMap(
                                data -> data,
                                data -> getTalentLevels(data.getProudSkillGroupId())
                        ));

        talents.put(
                getSkill(getSkillDepot().getEnergySkill()),
                getTalentLevels(getSkill(getSkillDepot().getEnergySkill()).getProudSkillGroupId())
        );
        return talents;
    }

    public List<ProudSkillExcelConfigDataJson> getPassives() {
        return getSkillDepot().getInherentProudSkillOpens()
                .stream()
                .filter(ascension -> ascension.getNeedAvatarPromoteLevel() <= currentAscension)
                .map(passive -> getPassive(passive.getProudSkillGroupId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Integer> getLevelableSkills() {
        //Fragile, assumes talent IDs are in ascending order for normal, skill and burst
        return getTalents().keySet()
                .stream()
                .map(AvatarSkillExcelConfigDataJson::getId)
                .collect(Collectors.toList());
    }

    public String getSkillDetails() {
        StringBuilder stringBuilder = new StringBuilder();
        getLevelableSkills().forEach(skill -> stringBuilder.append(getSkillDetail(skill)));
        return stringBuilder.toString();
    }

    public String getSkillDetail(int skill) {
        StringBuilder stringBuilder = new StringBuilder();
        Interpolator interpolator = new Interpolator();

        int level = currentTalentLevels.get(skill);
        AvatarSkillExcelConfigDataJson skillDetails = getSkill(skill);
        ProudSkillExcelConfigDataJson levelDetails = getTalentLevels(skillDetails.getProudSkillGroupId())
                .get(level);

        stringBuilder
                .append(Database.getInstance().getTranslation(skillDetails.getNameTextMapHash()))
                .append("\n")
                .append(Database.getInstance().getTranslation(skillDetails.getDescTextMapHash()))
                .append("\n")
                .append(format("Level: %s\n", level));

        levelDetails.getParamDescList()
                .forEach(parameter -> {
                    String value = Database.getInstance().getTranslation(parameter);
                    if (value != null) {
                        stringBuilder
                                .append(interpolator.interpolate(value, levelDetails.getParamList()))
                                .append("\n");
                    }
                });

        return stringBuilder.toString();
    }

    public String getPassiveDetail() {
        StringBuilder stringBuilder = new StringBuilder();
        getPassives().forEach(passive -> stringBuilder.append(getPassiveDetail(passive)));
        return stringBuilder.toString();
    }

    public String getPassiveDetail(ProudSkillExcelConfigDataJson passive) {
        StringBuilder stringBuilder = new StringBuilder();
        Interpolator interpolator = new Interpolator();

        stringBuilder
                .append(Database.getInstance().getTranslation(passive.getNameTextMapHash()))
                .append("\n")
                .append(Database.getInstance().getTranslation(passive.getDescTextMapHash()))
                .append("\n");

        passive.getParamDescList()
                .forEach(parameter -> {
                    String value = Database.getInstance().getTranslation(parameter);
                    if (value != null) {
                        stringBuilder
                                .append(interpolator.interpolate(value, passive.getParamList()))
                                .append("\n");
                    }
                });

        return stringBuilder.toString();
    }

    public Map<Integer, String> getConstellations() {
        List<AvatarTalentExcelConfigDataJson> constellations = getSkillDepot().getTalents()
                .stream()
                .filter(id -> id != 0)
                .map(this::getConstellation)
                .toList();

        return IntStream.range(0, constellations.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> i + 1,
                        i -> {
                            AvatarTalentExcelConfigDataJson current = constellations.get(i);
                            return String.format("%s. %s\n%s\n",
                                    i + 1,
                                    Database.getInstance().getTranslation(current.getNameTextMapHash()),
                                    Database.getInstance().getTranslation(current.getDescTextMapHash()));
                        },
                        (a, b) -> b, //There are no duplicates, but need to specify something to select LinkedHashMap
                        LinkedHashMap::new
                ));
    }

    public Map<String, Integer> getAllAscensionItems() {
        return getAscensionItems(true).entrySet()
                .stream()
                .filter(id -> id.getKey() != 0)
                .collect(Collectors.toMap(
                        item -> Database.getInstance().getTranslation(getItem(item.getKey()).getNameTextMapHash()),
                        Map.Entry::getValue
                ));
    }

    public Integer getAllAscensionCosts() {
        return getAscensionCost(true);
    }

    public Map<String, Integer> getAllTalentItems() {
        return getTalents().values()
                .stream()
                .flatMap(map -> map.values().stream())
                .flatMap(costs -> costs.getCostItems().stream())
                .filter(id -> id.getId() != 0)
                .collect(Collectors.toMap(
                        item -> Database.getInstance().getTranslation(getItem(item.getId()).getNameTextMapHash()),
                        dev.xfj.jsonschema2pojo.proudskillexcelconfigdata.CostItem::getCount,
                        Integer::sum
                ));
    }

    public Integer getAllTalentCosts() {
        return getTalents().values()
                .stream()
                .flatMap(map -> map.values().stream())
                .mapToInt(ProudSkillExcelConfigDataJson::getCoinCost)
                .sum();
    }

    public Map<String, Integer> getAllExpBooks() {
        Map<String, Integer> result = new LinkedHashMap<>();

        Map<Integer, AvatarPromoteExcelConfigDataJson> ascensions = getAscensions(getAvatar().getAvatarPromoteId());
        int nextStartingLevel = 1;

        for (int i = 0; i < ascensions.size(); i++) {
            AvatarPromoteExcelConfigDataJson current = ascensions.get(i);
            int currentMaxLevel = current.getUnlockMaxLevel();
            getExpBooksForLevel(nextStartingLevel, currentMaxLevel).forEach((id, count) -> result.merge(id, count, Integer::sum));
            nextStartingLevel = currentMaxLevel;
        }

        return result;
    }

    public Integer getAllExpCosts() {
        Map<String, Integer> expBooks = getExpBooks().keySet()
                .stream()
                .map(this::getItem)
                .collect(Collectors.toMap(
                        book -> Database.getInstance().getTranslation(book.getNameTextMapHash()),
                        MaterialExcelConfigDataJson::getId
                ));

        return getAllExpBooks().entrySet()
                .stream()
                .mapToInt(cost -> getCostForExpItem(expBooks.get(cost.getKey())) * cost.getValue())
                .sum();
    }

    public Map<String, Integer> getAllItemRequirements() {
        Map<String, Integer> result = getAllAscensionItems();
        getAllTalentItems().forEach((id, count) -> result.merge(id, count, Integer::sum));
        getAllExpBooks().forEach((id, count) -> result.merge(id, count, Integer::sum));
        return result;
    }

    public Integer getAllItemCosts() {
        return getAllAscensionCosts() + getAllTalentCosts() + getAllExpCosts();
    }

    private double getBaseStat(double baseValue, String statType) {
        return (baseValue * getBaseStatMultiplier(statType)) + getExtraBaseStats(statType);
    }

    private double getExtraBaseStats(String selected) {
        return getAscensions(getAvatar().getAvatarPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() == currentAscension)
                .flatMap(promotions -> promotions.getAddProps().stream())
                .filter(prop -> prop.getPropType().equals(selected))
                .mapToDouble(AddProp::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getCurveMultiplier(String curveType) {
        return AvatarData.getInstance().avatarCurveConfig
                .stream()
                .filter(level -> level.getLevel() == currentLevel)
                .flatMap(curves -> curves.getCurveInfos().stream())
                .filter(curve -> curve.getType().equals(curveType))
                .map(CurveInfo::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getBaseStatMultiplier(String selected) {
        return getAvatar().getPropGrowCurves()
                .stream()
                .filter(curves -> curves.getType().equals(selected))
                .map(curve -> getCurveMultiplier(curve.getGrowCurve()))
                .findFirst()
                .orElse(-1.0);
    }

    private AvatarExcelConfigDataJson getAvatar() {
        return AvatarData.getInstance().avatarConfig
                .stream()
                .filter(character -> character.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private FetterInfoExcelConfigDataJson getFetter() {
        return AvatarData.getInstance().fetterInfoConfig
                .stream()
                .filter(character -> character.getAvatarId() == id)
                .findFirst()
                .orElse(null);
    }

    private Map<Integer, AvatarPromoteExcelConfigDataJson> getAscensions(int promoteId) {
        return AvatarData.getInstance().avatarPromoteConfig
                .stream()
                .filter(ascension -> ascension.getAvatarPromoteId() == promoteId)
                .collect(Collectors.toMap(
                        AvatarPromoteExcelConfigDataJson::getPromoteLevel,
                        data -> data
                ));
    }

    private AvatarSkillDepotExcelConfigDataJson getSkillDepot() {
        return AvatarData.getInstance().skillDepotConfig
                .stream()
                .filter(depot -> depot.getId() == getAvatar().getSkillDepotId())
                .findFirst()
                .orElse(null);
    }

    private AvatarSkillExcelConfigDataJson getSkill(int id) {
        return AvatarData.getInstance().skillConfig
                .stream()
                .filter(skill -> skill.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private ProudSkillExcelConfigDataJson getPassive(int id) {
        return AvatarData.getInstance().proudSkillConfig
                .stream()
                .filter(passive -> passive.getProudSkillGroupId() == id)
                .findFirst()
                .orElse(null);
    }

    private Map<Integer, ProudSkillExcelConfigDataJson> getTalentLevels(int proudSkillGroupId) {
        return AvatarData.getInstance().proudSkillConfig
                .stream()
                .filter(talent -> talent.getProudSkillGroupId() == proudSkillGroupId)
                .collect(Collectors.toMap(
                        ProudSkillExcelConfigDataJson::getLevel,
                        data -> data
                ));
    }

    private AvatarTalentExcelConfigDataJson getConstellation(int id) {
        return AvatarData.getInstance().avatarTalentConfig
                .stream()
                .filter(constellation -> constellation.getTalentId() == id)
                .findFirst()
                .orElse(null);
    }

    private MaterialExcelConfigDataJson getItem(int id) {
        return ItemData.getInstance().materialConfig
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Map<Integer, Integer> getExpBooks() {
        return ItemData.getInstance().materialConfig
                .stream()
                .filter(item -> "MATERIAL_EXP_FRUIT".equals(item.getMaterialType()))
                .collect(Collectors.toMap(
                        MaterialExcelConfigDataJson::getId,
                        item -> item.getItemUse()
                                .stream()
                                .filter(use -> "ITEM_USE_ADD_EXP".equals(use.getUseOp()))
                                .flatMap(use -> use.getUseParam().stream())
                                .filter(param -> !param.isBlank())
                                .mapToInt(Integer::parseInt)
                                .sum()
                ));
    }

    private int getExpRequired(int startingLevel, int targetLevel) {
        return AvatarData.getInstance().levelConfig
                .stream()
                .filter(level -> level.getLevel() >= startingLevel)
                .filter(level -> level.getLevel() < targetLevel)
                .mapToInt(AvatarLevelExcelConfigDataJson::getExp)
                .sum();
    }

    private Map<String, Integer> getExpBooksForLevel(int startingLevel, int targetLevel) {
        int expRequired = getExpRequired(startingLevel, targetLevel);

        Map<Integer, Integer> expBooks = getExpBooks().entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (entryA, entryB) -> entryA,
                        LinkedHashMap::new));

        Map<String, Integer> result = new LinkedHashMap<>();
        int expRemaining = expRequired;

        Iterator<Map.Entry<Integer, Integer>> iterator = expBooks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();

            if (expRemaining <= 0) {
                break;
            }

            int expProvided = entry.getValue();
            int booksNeeded = expRemaining / expProvided;

            if (!iterator.hasNext()) {
                booksNeeded++;
            }

            result.put(Database.getInstance().getTranslation(getItem(entry.getKey()).getNameTextMapHash()), booksNeeded);
            expRemaining -= booksNeeded * expProvided;
        }

        checkExpBookEfficiency(result);

        return result;
    }

    private void checkExpBookEfficiency(Map<String, Integer> input) {
        List<Integer> expBookIds = getExpBooks().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();

        for (int i = 0; i < expBookIds.size() - 1; i++) {
            String current = Database.getInstance().getTranslation(getItem(expBookIds.get(i)).getNameTextMapHash());
            String next = Database.getInstance().getTranslation(getItem(expBookIds.get(i + 1)).getNameTextMapHash());

            if (getCostForExpItem(expBookIds.get(i)) * input.get(current) >= getCostForExpItem(expBookIds.get(i + 1))) {
                input.put(current, 0);
                input.put(next, input.get(next) + 1);
            }
        }
    }

    private int getCostForExpItem(int id) {
        Map<Integer, Integer> expBooks = getExpBooks();
        expBooks.replaceAll((book, exp) -> exp / 5);

        return expBooks.entrySet()
                .stream()
                .filter(item -> item.getKey() == id)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentExperience() {
        return currentExperience;
    }

    public void setCurrentExperience(int currentExperience) {
        this.currentExperience = currentExperience;
    }

    public int getCurrentAscension() {
        return currentAscension;
    }

    public void setCurrentAscension(int currentAscension) {
        this.currentAscension = currentAscension;
    }

    public Map<Integer, Integer> getCurrentTalentLevels() {
        return currentTalentLevels;
    }

    public void setCurrentTalentLevels(Map<Integer, Integer> currentTalentLevels) {
        this.currentTalentLevels = currentTalentLevels;
    }

    public void setCurrentTalentLevel(int skillIndex, int skillLevel) {
        currentTalentLevels.put(getLevelableSkills().get(skillIndex), skillLevel);
    }
}
