package dev.xfj.core.container;

import dev.xfj.core.constants.CharacterRarity;
import dev.xfj.core.dto.character.*;
import dev.xfj.core.services.DatabaseService;
import dev.xfj.core.utils.KeyValue;
import dev.xfj.jsonschema2pojo.avatarcostumeexcelconfigdata.AvatarCostumeExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarcurveexcelconfigdata.CurveInfo;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AddProp;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AvatarPromoteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.CostItem;
import dev.xfj.jsonschema2pojo.avatarskilldepotexcelconfigdata.AvatarSkillDepotExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarskillexcelconfigdata.AvatarSkillExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatartalentexcelconfigdata.AvatarTalentExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fettercharactercardexcelconfigdata.FetterCharacterCardExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fetterinfoexcelconfigdata.FetterInfoExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fettersexcelconfigdata.FettersExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fetterstoryexcelconfigdata.FetterStoryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.furnituresuiteexcelconfigdata.FurnitureSuiteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldfurnitureexcelconfigdata.HomeWorldFurnitureExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldnpcexcelconfigdata.HomeWorldNPCExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.proudskillexcelconfigdata.ProudSkillExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.rewardexcelconfigdata.RewardItem;
import dev.xfj.core.utils.Interpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;

@Component
@Scope("prototype")
public class CharacterContainer implements Container, Ascendable {
    private final DatabaseService databaseService;
    private static final String BASE_HP = "FIGHT_PROP_BASE_HP";
    private static final String BASE_DEF = "FIGHT_PROP_BASE_DEFENSE";
    private static final String BASE_ATK = "FIGHT_PROP_BASE_ATTACK";
    private int id;
    private int currentLevel;
    private int currentExperience;
    private int currentAscension;
    private Map<Integer, Integer> currentTalentLevels;


    @Autowired
    public CharacterContainer(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return databaseService.getTranslation(getAvatar().getNameTextMapHash());
    }

    public String getTitle() {
        return databaseService.getTranslation(getFetterInfo().getAvatarTitleTextMapHash());
    }

    @Override
    public Integer getRarity() {
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

    @Override
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
                        stat -> getManualMappedText(stat.getPropType()),
                        AddProp::getValue
                ));
    }

    @Override
    public Map<Integer, Integer> getAscensionItems() {
        if (currentAscension < getMaxAscensions(getAvatar().getAvatarPromoteId())) {
            return getAscensionItems(currentAscension, currentAscension + 1);
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public Map<Integer, Integer> getAscensionItems(int startingAscension, int targetAscension) {
        return getAscensions(getAvatar().getAvatarPromoteId()).values()
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
        if (currentAscension < getMaxAscensions(getAvatar().getAvatarPromoteId())) {
            return getAscensionCost(currentAscension, currentAscension + 1);
        } else {
            return 0;
        }
    }

    @Override
    public Integer getAscensionCost(int startingAscension, int targetAscension) {
        return getAscensions(getAvatar().getAvatarPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() > startingAscension)
                .filter(ascension -> ascension.getPromoteLevel() <= targetAscension)
                .mapToInt(AvatarPromoteExcelConfigDataJson::getScoinCost)
                .sum();
    }

    public String getVision() {
        return databaseService.getTranslation(getFetterInfo().getAvatarVisionBeforTextMapHash());
    }

    public String getWeaponType() {
        return getManualMappedText(getAvatar().getWeaponType());
    }

    public String getConstellation() {
        return databaseService.getTranslation(getFetterInfo().getAvatarConstellationBeforTextMapHash());
    }

    public String getNative() {
        return databaseService.getTranslation(getFetterInfo().getAvatarNativeTextMapHash());
    }

    public String getBirthday() {
        return format("%1$2s/%2$2s",
                getFetterInfo().getInfoBirthMonth(), getFetterInfo().getInfoBirthDay()).replace(' ', '0');
    }

    public String getVA(String language) {
        return switch (language) {
            case "EN" -> databaseService.getTranslation(getFetterInfo().getCvEnglishTextMapHash());
            case "JP" -> databaseService.getTranslation(getFetterInfo().getCvJapaneseTextMapHash());
            case "CHS" -> databaseService.getTranslation(getFetterInfo().getCvChineseTextMapHash());
            case "KR" -> databaseService.getTranslation(getFetterInfo().getCvKoreanTextMapHash());
            default -> "No VA available";
        };
    }

    @Override
    public String getDescription() {
        return databaseService.getTranslation(getFetterInfo().getAvatarDetailTextMapHash());
    }

    public Map<AvatarSkillExcelConfigDataJson, Map<Integer, ProudSkillExcelConfigDataJson>> getTalents() {
        Map<AvatarSkillExcelConfigDataJson, Map<Integer, ProudSkillExcelConfigDataJson>> talents =
                getSkillDepot().getSkills()
                        .stream()
                        .filter(id -> id != 0)
                        .map(this::getSkill)
                        .collect(Collectors.toMap(
                                data -> data,
                                data -> getTalentLevels(data.getProudSkillGroupId()),
                                (a, b) -> b,
                                LinkedHashMap::new
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

    public List<TalentDTO> getSkillDetails() {
        return getLevelableSkills()
                .stream()
                .map(this::getSkillDetail)
                .filter(Objects::nonNull)
                .toList();
    }

    public TalentDTO getSkillDetail(int skill) {
        int level = currentTalentLevels.get(skill);
        AvatarSkillExcelConfigDataJson skillDetails = getSkill(skill);

        if (skillDetails.getProudSkillGroupId() != 0) {
            ProudSkillExcelConfigDataJson levelDetails = getTalentLevels(skillDetails.getProudSkillGroupId())
                    .get(level);

            return new TalentDTO(
                    skillDetails.getId(),
                    databaseService.getTranslation(skillDetails.getNameTextMapHash()),
                    databaseService.getTranslation(skillDetails.getDescTextMapHash()),
                    level,
                    levelDetails.getParamDescList()
                            .stream()
                            .map(databaseService::getTranslation)
                            .filter(Objects::nonNull)
                            .map(value -> {
                                String[] entry = new Interpolator().interpolate(value, levelDetails.getParamList())
                                        .split("\\|");
                                return new KeyValue(entry[0], entry[1]);
                            })
                            .collect(Collectors.toList())
            );
        }

        return null;
    }

    public List<PassiveDTO> getPassiveDetails() {
        return getPassives()
                .stream()
                .map(this::getPassiveDetails)
                .filter(Objects::nonNull)
                .toList();
    }

    public PassiveDTO getPassiveDetails(ProudSkillExcelConfigDataJson passive) {
        return new PassiveDTO(
                passive.getProudSkillId(),
                databaseService.getTranslation(passive.getNameTextMapHash()),
                databaseService.getTranslation(passive.getDescTextMapHash()),
                passive.getParamDescList()
                        .stream()
                        .map(databaseService::getTranslation)
                        .filter(Objects::nonNull)
                        .map(value -> {
                            String[] entry = new Interpolator().interpolate(value, passive.getParamList())
                                    .split("\\|");
                            return new KeyValue(entry[0], entry[1]);
                        })
                        .collect(Collectors.toList())
        );
    }

    public List<ConstellationDTO> getConstellations() {
        List<AvatarTalentExcelConfigDataJson> constellations = getSkillDepot().getTalents()
                .stream()
                .filter(id -> id != 0)
                .map(this::getConstellation)
                .toList();

        return IntStream.range(0, constellations.size())
                .boxed()
                .map(i -> {
                    AvatarTalentExcelConfigDataJson current = constellations.get(i);
                    return new ConstellationDTO(
                            i + 1,
                            databaseService.getTranslation(current.getNameTextMapHash()),
                            databaseService.getTranslation(current.getDescTextMapHash())
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getAllAscensionItems() {
        return getAscensionItems(0, getMaxAscensions(getAvatar().getAvatarPromoteId())).entrySet()
                .stream()
                .filter(id -> id.getKey() != 0)
                .collect(Collectors.toMap(
                        item -> databaseService.getTranslation(getItem(item.getKey()).getNameTextMapHash()),
                        Map.Entry::getValue
                ));
    }

    @Override
    public Integer getAllAscensionCosts() {
        return getAscensionCost(0, getMaxAscensions(getAvatar().getAvatarPromoteId()));
    }

    public Map<String, Integer> getAllTalentItems() {
        return getTalents().values()
                .stream()
                .flatMap(map -> map.values().stream())
                .flatMap(costs -> costs.getCostItems().stream())
                .filter(id -> id.getId() != 0)
                .collect(Collectors.toMap(
                        item -> databaseService.getTranslation(getItem(item.getId()).getNameTextMapHash()),
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

    @Override
    public Integer getMaxLevel() {
        return databaseService.levelConfig
                .stream()
                .max(Comparator.comparing(AvatarLevelExcelConfigDataJson::getLevel))
                .stream()
                .mapToInt(AvatarLevelExcelConfigDataJson::getLevel)
                .findFirst()
                .orElse(-1);
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
                        book -> databaseService.getTranslation(book.getNameTextMapHash()),
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

    public NameDescriptionDTO getNamecardDescription() {
        MaterialExcelConfigDataJson card = getCharacterCard();

        return new NameDescriptionDTO(
                databaseService.getTranslation(card.getNameTextMapHash()),
                databaseService.getTranslation(card.getDescTextMapHash())
        );
    }

    public NameDescriptionDTO getSpecialtyFoodName() {
        return new NameDescriptionDTO(
                databaseService.getTranslation(getSpecialtyFood().getNameTextMapHash()),
                databaseService.getTranslation(getSpecialtyFood().getDescTextMapHash())
        );
    }

    public List<NameDescriptionDTO> getOutfits() {
        return getCostumes()
                .stream()
                .map(costume -> new NameDescriptionDTO(
                        databaseService.getTranslation(costume.getNameTextMapHash()),
                        databaseService.getTranslation(costume.getDescTextMapHash()))
                )
                .collect(Collectors.toList());
    }

    public TeaPotDTO getTeaPotDetails() {
        HomeWorldFurnitureExcelConfigDataJson furniture = getFurnitureDetails();
        
        return new TeaPotDTO(
                furniture.getFurnType()
                        .stream()
                        .map(this::getFurnitureType)
                        .collect(Collectors.toList()),
                furniture.getComfort(),
                furniture.getCost(),
                furniture.getRankLevel(),
                databaseService.getTranslation(furniture.getDescTextMapHash()),
                getPreferredFurnitureSets()
                        .stream()
                        .map(set -> new NameDescriptionDTO(databaseService.getTranslation(set.getSuiteNameTextMapHash()), databaseService.getTranslation(set.getSuiteDescTextMapHash())))
                        .collect(Collectors.toList())
        );
    }

    public String getStories() {
        StringBuilder stringBuilder = new StringBuilder();
        getFetterStories()
                .forEach(story -> {
                    stringBuilder.append(databaseService.getTranslation(story.getStoryTitleTextMapHash())).append("\n");
                    if (story.getOpenConds()
                            .stream()
                            .anyMatch(condition -> "FETTER_COND_FETTER_LEVEL".equals(condition.getCondType()))
                    ) {
                        stringBuilder.append(databaseService.getTranslation(story.getTips()
                                .stream()
                                .findFirst()
                                .orElse(null))).append("\n");
                    }

                    stringBuilder.append(databaseService.getTranslation(story.getStoryContextTextMapHash())).append("\n");
                });

        return stringBuilder.toString();
    }

    public String getQuotes() {
        StringBuilder stringBuilder = new StringBuilder();
        getFetters()
                .forEach(story -> {
                    stringBuilder.append(databaseService.getTranslation(story.getVoiceTitleTextMapHash())).append("\n");
                    if (story.getOpenConds()
                            .stream()
                            .anyMatch(condition -> "FETTER_COND_FETTER_LEVEL".equals(condition.getCondType()))
                    ) {
                        stringBuilder.append(databaseService.getTranslation(story.getTips()
                                .stream()
                                .findFirst()
                                .orElse(null))).append("\n");
                    }

                    stringBuilder.append(databaseService.getTranslation(story.getVoiceFileTextTextMapHash())).append("\n");
                });

        return stringBuilder.toString();
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
        return databaseService.avatarCurveConfig
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
        return databaseService.avatarConfig
                .stream()
                .filter(character -> character.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private FetterInfoExcelConfigDataJson getFetterInfo() {
        return databaseService.fetterInfoConfig
                .stream()
                .filter(character -> character.getAvatarId() == id)
                .findFirst()
                .orElse(null);
    }

    private Map<Integer, AvatarPromoteExcelConfigDataJson> getAscensions(int promoteId) {
        return databaseService.avatarPromoteConfig
                .stream()
                .filter(ascension -> ascension.getAvatarPromoteId() == promoteId)
                .collect(Collectors.toMap(
                        AvatarPromoteExcelConfigDataJson::getPromoteLevel,
                        data -> data
                ));
    }

    public int getMaxAscensions() {
        return getMaxAscensions(getAvatar().getAvatarPromoteId());
    }

    private int getMaxAscensions(int promoteId) {
        return databaseService.avatarPromoteConfig
                .stream()
                .filter(ascension -> ascension.getAvatarPromoteId() == promoteId)
                .max(Comparator.comparing(AvatarPromoteExcelConfigDataJson::getPromoteLevel))
                .stream()
                .mapToInt(AvatarPromoteExcelConfigDataJson::getPromoteLevel)
                .findFirst()
                .orElse(-1);
    }

    private AvatarSkillDepotExcelConfigDataJson getSkillDepot() {
        return databaseService.skillDepotConfig
                .stream()
                .filter(depot -> depot.getId() == getAvatar().getSkillDepotId())
                .findFirst()
                .orElse(null);
    }

    private AvatarSkillExcelConfigDataJson getSkill(int id) {
        return databaseService.skillConfig
                .stream()
                .filter(skill -> skill.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private ProudSkillExcelConfigDataJson getPassive(int id) {
        return databaseService.proudSkillConfig
                .stream()
                .filter(passive -> passive.getProudSkillGroupId() == id)
                .findFirst()
                .orElse(null);
    }

    private Map<Integer, ProudSkillExcelConfigDataJson> getTalentLevels(int proudSkillGroupId) {
        return databaseService.proudSkillConfig
                .stream()
                .filter(talent -> talent.getProudSkillGroupId() == proudSkillGroupId)
                .collect(Collectors.toMap(
                        ProudSkillExcelConfigDataJson::getLevel,
                        data -> data
                ));
    }

    private AvatarTalentExcelConfigDataJson getConstellation(int id) {
        return databaseService.avatarTalentConfig
                .stream()
                .filter(constellation -> constellation.getTalentId() == id)
                .findFirst()
                .orElse(null);
    }

    private Map<Integer, Integer> getExpBooks() {
        return databaseService.materialConfig
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
        return databaseService.levelConfig
                .stream()
                .filter(level -> level.getLevel() >= startingLevel)
                .filter(level -> level.getLevel() < targetLevel)
                .mapToInt(AvatarLevelExcelConfigDataJson::getExp)
                .sum();
    }

    private Map<String, Integer> getExpBooksForLevel(int startingLevel, int targetLevel) {
        int expRequired = getExpRequired(startingLevel, targetLevel) - currentExperience;

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

            result.put(databaseService.getTranslation(getItem(entry.getKey()).getNameTextMapHash()), booksNeeded);
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
            String current = databaseService.getTranslation(getItem(expBookIds.get(i)).getNameTextMapHash());
            String next = databaseService.getTranslation(getItem(expBookIds.get(i + 1)).getNameTextMapHash());

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

    private MaterialExcelConfigDataJson getCharacterCard() {
        return getItem(databaseService.rewardConfig
                .stream()
                .filter(reward -> reward.getRewardId() == databaseService.fetterCharacterCardConfig
                        .stream()
                        .filter(character -> character.getAvatarId() == getAvatar().getId())
                        .mapToInt(FetterCharacterCardExcelConfigDataJson::getRewardId)
                        .findFirst()
                        .orElse(-1))
                .mapToInt(item -> item.getRewardItemList()
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToInt(RewardItem::getItemId)
                        .findFirst()
                        .orElse(-1))
                .findFirst()
                .orElse(-1));
    }

    private MaterialExcelConfigDataJson getSpecialtyFood() {
        return getItem(databaseService.cookBonusConfig
                .stream()
                .filter(id -> id.getAvatarId() == getAvatar().getId())
                .flatMap(entry -> entry.getParamVec().stream())
                .filter(param -> param != 0)
                .findFirst()
                .orElse(-1));
    }

    private List<AvatarCostumeExcelConfigDataJson> getCostumes() {
        return databaseService.avatarCostumeConfig
                .stream()
                .filter(id -> id.getCharacterId() == getAvatar().getId())
                .collect(Collectors.toList());
    }

    private int getFurnitureId() {
        return databaseService.homeWorldNPCConfig
                .stream()
                .filter(id -> id.getAvatarID() == getAvatar().getId())
                .mapToInt(HomeWorldNPCExcelConfigDataJson::getFurnitureID)
                .findFirst()
                .orElse(-1);
    }

    private HomeWorldFurnitureExcelConfigDataJson getFurnitureDetails() {
        return databaseService.homeWorldFurnitureConfig
                .stream()
                .filter(id -> id.getId() == getFurnitureId())
                .findFirst()
                .orElse(null);
    }

    private String getFurnitureType(int id) {
        return databaseService.homeWorldFurnitureTypeConfig
                .stream()
                .filter(type -> type.getTypeID() == id)
                .map(name -> databaseService.getTranslation(name.getTypeNameTextMapHash()))
                .findFirst()
                .orElse(null);
    }

    private List<FurnitureSuiteExcelConfigDataJson> getPreferredFurnitureSets() {
        return databaseService.furnitureSuiteConfig
                .stream()
                .filter(furniture -> furniture.getFavoriteNpcExcelIdVec().contains(getAvatar().getId()))
                .collect(Collectors.toList());
    }

    private List<FetterStoryExcelConfigDataJson> getFetterStories() {
        return databaseService.fetterStoryConfig
                .stream()
                .filter(id -> id.getAvatarId() == getAvatar().getId())
                .collect(Collectors.toList());
    }

    private List<FettersExcelConfigDataJson> getFetters() {
        return databaseService.fettersConfig
                .stream()
                .filter(id -> id.getAvatarId() == getAvatar().getId())
                .collect(Collectors.toList());
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

    public void setCurrentTalentLevels(int skillLevel) {
        currentTalentLevels = getTalents().keySet()
                .stream()
                .map(AvatarSkillExcelConfigDataJson::getId)
                .collect(Collectors.toMap(
                        skillId -> skillId,
                        level -> skillLevel
                ));
    }
}
