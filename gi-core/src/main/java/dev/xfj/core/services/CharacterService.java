package dev.xfj.core.services;

import dev.xfj.core.constants.CharacterRarity;
import dev.xfj.core.dto.character.*;
import dev.xfj.core.dto.codex.CharacterCodexDTO;
import dev.xfj.core.logic.specification.CharacterSpecification;
import dev.xfj.core.utils.Interpolator;
import dev.xfj.core.utils.KeyValue;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.xfj.core.services.DatabaseWrapper.getItem;
import static dev.xfj.core.services.DatabaseWrapper.getManualMappedText;
import static java.lang.String.format;

@Service
public class CharacterService {
    private final DatabaseService databaseService;
    private static final String BASE_HP = "FIGHT_PROP_BASE_HP";
    private static final String BASE_DEF = "FIGHT_PROP_BASE_DEFENSE";
    private static final String BASE_ATK = "FIGHT_PROP_BASE_ATTACK";

    @Autowired
    public CharacterService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<CharacterCodexDTO> getCharacters() {
        return databaseService.avatarCodexConfig
                .stream()
                .sorted(Comparator.comparing(AvatarCodexExcelConfigDataJson::getSortFactor))
                .map(entry -> new CharacterCodexDTO(
                        entry.getAvatarId(),
                        databaseService.avatarConfig
                                .stream()
                                .filter(character -> character.getId() == entry.getAvatarId())
                                .findFirst()
                                .map(hash -> databaseService.getTranslation(hash.getNameTextMapHash()))
                                .orElse(""),
                        LocalDateTime.parse(entry.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        entry.getSortFactor())
                )
                .collect(Collectors.toList());
    }

    public CharacterProfileDTO getCharacter(int characterId, int level, int experience, int ascension) {
        CharacterSpecification character = new CharacterSpecification();
        character.id = characterId;
        character.currentLevel = level;
        character.currentExperience = experience;
        character.currentAscension = ascension;

        return new CharacterProfileDTO(
                character.id,
                getName(character),
                getTitle(character),
                getRarity(character),
                getBaseHealth(character),
                getBaseAttack(character),
                getBaseDefense(character),
                getAscensionStat(character).entrySet().stream()
                        .findFirst()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .orElse(new KeyValue("", null)),
                new RequirementsDTO(getAscensionItems(character).entrySet()
                        .stream()
                        .filter(entry -> entry.getKey() != 0)
                        .map(entry -> new KeyValue(
                                databaseService.getTranslation(
                                        DatabaseWrapper.getItem(entry.getKey()).getNameTextMapHash()
                                ),
                                entry.getValue())
                        )
                        .collect(Collectors.toList()),
                        getAscensionCost(character)),
                getVision(character),
                getWeaponType(character),
                getConstellation(character),
                getNative(character),
                getBirthday(character),
                new VoiceActorDTO(
                        getVA(character, "EN"),
                        getVA(character, "CHS"),
                        getVA(character, "JP"),
                        getVA(character, "KR")
                ),
                getDescription(character),
                getNamecardDescription(character),
                getSpecialtyFoodName(character),
                getOutfits(character)
        );
    }

    public TalentsDTO getTalents(int characterId, int level) {
        CharacterSpecification character = new CharacterSpecification();
        character.id = characterId;
        character.currentLevel = getMaxLevel();
        character.currentAscension = getMaxAscensions(character);
        setCurrentTalentLevels(character, level);

        return new TalentsDTO(getSkillDetails(character), getPassiveDetails(character));
    }

    public List<ConstellationDTO> getConstellations(int characterId) {
        CharacterSpecification character = new CharacterSpecification();
        character.id = characterId;

        return getConstellations(character);
    }

    public MaterialsDTO getMaterials(int characterId) {
        CharacterSpecification character = new CharacterSpecification();
        character.id = characterId;

        return new MaterialsDTO(
                new RequirementsDTO(getAllAscensionItems(character).entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        getAllAscensionCosts(character)),
                new RequirementsDTO(getAllExpBooks(character).entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        getAllExpCosts(character)),
                new RequirementsDTO(getAllTalentItems(character).entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        getAllTalentCosts(character)),
                new RequirementsDTO(getAllItemRequirements(character).entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        getAllItemCosts(character))
        );
    }

    public TeaPotDTO getTeaPotDetails(int characterId) {
        CharacterSpecification character = new CharacterSpecification();
        character.id = characterId;

        return getTeaPotDetails(character);
    }

    public List<StoryDTO> getStories(int characterId) {
        CharacterSpecification character = new CharacterSpecification();
        character.id = characterId;

        return getStories(character);
    }

    public List<StoryDTO> getQuotes(int characterId) {
        CharacterSpecification character = new CharacterSpecification();
        character.id = characterId;

        return getQuotes(character);
    }

    private String getName(CharacterSpecification characterSpecification) {
        return databaseService.getTranslation(getAvatar(characterSpecification).getNameTextMapHash());
    }

    private String getTitle(CharacterSpecification characterSpecification) {
        return databaseService.getTranslation(getFetterInfo(characterSpecification).getAvatarTitleTextMapHash());
    }

    private Integer getRarity(CharacterSpecification characterSpecification) {
        return CharacterRarity.valueOf(getAvatar(characterSpecification).getQualityType()).getStarValue();
    }

    private double getBaseHealth(CharacterSpecification characterSpecification) {
        return getBaseStat(characterSpecification, getAvatar(characterSpecification).getHpBase(), BASE_HP);
    }

    private double getBaseAttack(CharacterSpecification characterSpecification) {
        return getBaseStat(characterSpecification, getAvatar(characterSpecification).getAttackBase(), BASE_ATK);
    }

    private double getBaseDefense(CharacterSpecification characterSpecification) {
        return getBaseStat(characterSpecification, getAvatar(characterSpecification).getDefenseBase(), BASE_DEF);
    }

    private double getBaseCritRate(CharacterSpecification characterSpecification) {
        return getAvatar(characterSpecification).getCritical();
    }

    private double getBaseCritDamage(CharacterSpecification characterSpecification) {
        return getAvatar(characterSpecification).getCriticalHurt();
    }

    private Map<String, Double> getAscensionStat(CharacterSpecification characterSpecification) {
        return getAscensions(getAvatar(characterSpecification).getAvatarPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() == characterSpecification.currentAscension)
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

    private Map<Integer, Integer> getAscensionItems(CharacterSpecification characterSpecification) {
        if (characterSpecification.currentAscension < getMaxAscensions(getAvatar(characterSpecification).getAvatarPromoteId())) {
            return getAscensionItems(characterSpecification, characterSpecification.currentAscension, characterSpecification.currentAscension + 1);
        } else {
            return new HashMap<>();
        }
    }

    private Map<Integer, Integer> getAscensionItems(CharacterSpecification characterSpecification, int startingAscension, int targetAscension) {
        return getAscensions(getAvatar(characterSpecification).getAvatarPromoteId()).values()
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

    private Integer getAscensionCost(CharacterSpecification characterSpecification) {
        if (characterSpecification.currentAscension < getMaxAscensions(getAvatar(characterSpecification).getAvatarPromoteId())) {
            return getAscensionCost(characterSpecification, characterSpecification.currentAscension, characterSpecification.currentAscension + 1);
        } else {
            return 0;
        }
    }

    private Integer getAscensionCost(CharacterSpecification characterSpecification, int startingAscension, int targetAscension) {
        return getAscensions(getAvatar(characterSpecification).getAvatarPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() > startingAscension)
                .filter(ascension -> ascension.getPromoteLevel() <= targetAscension)
                .mapToInt(AvatarPromoteExcelConfigDataJson::getScoinCost)
                .sum();
    }

    private String getVision(CharacterSpecification characterSpecification) {
        return databaseService.getTranslation(getFetterInfo(characterSpecification).getAvatarVisionBeforTextMapHash());
    }

    private String getWeaponType(CharacterSpecification characterSpecification) {
        return getManualMappedText(getAvatar(characterSpecification).getWeaponType());
    }

    private String getConstellation(CharacterSpecification characterSpecification) {
        return databaseService.getTranslation(getFetterInfo(characterSpecification).getAvatarConstellationBeforTextMapHash());
    }

    private String getNative(CharacterSpecification characterSpecification) {
        return databaseService.getTranslation(getFetterInfo(characterSpecification).getAvatarNativeTextMapHash());
    }

    private String getBirthday(CharacterSpecification characterSpecification) {
        return format("%1$2s/%2$2s",
                getFetterInfo(characterSpecification).getInfoBirthMonth(), getFetterInfo(characterSpecification).getInfoBirthDay()).replace(' ', '0');
    }

    private String getVA(CharacterSpecification characterSpecification, String language) {
        return switch (language) {
            case "EN" ->
                    databaseService.getTranslation(getFetterInfo(characterSpecification).getCvEnglishTextMapHash());
            case "JP" ->
                    databaseService.getTranslation(getFetterInfo(characterSpecification).getCvJapaneseTextMapHash());
            case "CHS" ->
                    databaseService.getTranslation(getFetterInfo(characterSpecification).getCvChineseTextMapHash());
            case "KR" -> databaseService.getTranslation(getFetterInfo(characterSpecification).getCvKoreanTextMapHash());
            default -> "No VA available";
        };
    }

    private String getDescription(CharacterSpecification characterSpecification) {
        return databaseService.getTranslation(getFetterInfo(characterSpecification).getAvatarDetailTextMapHash());
    }

    private Map<AvatarSkillExcelConfigDataJson, Map<Integer, ProudSkillExcelConfigDataJson>> getTalents(CharacterSpecification characterSpecification) {
        Map<AvatarSkillExcelConfigDataJson, Map<Integer, ProudSkillExcelConfigDataJson>> talents =
                getSkillDepot(characterSpecification).getSkills()
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
                getSkill(getSkillDepot(characterSpecification).getEnergySkill()),
                getTalentLevels(getSkill(getSkillDepot(characterSpecification).getEnergySkill()).getProudSkillGroupId())
        );
        return talents;
    }

    private List<ProudSkillExcelConfigDataJson> getPassives(CharacterSpecification characterSpecification) {
        return getSkillDepot(characterSpecification).getInherentProudSkillOpens()
                .stream()
                .filter(ascension -> ascension.getNeedAvatarPromoteLevel() <= characterSpecification.currentAscension)
                .map(passive -> getPassive(passive.getProudSkillGroupId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Integer> getLevelableSkills(CharacterSpecification characterSpecification) {
        //Fragile, assumes talent IDs are in ascending order for normal, skill and burst
        return getTalents(characterSpecification).keySet()
                .stream()
                .map(AvatarSkillExcelConfigDataJson::getId)
                .collect(Collectors.toList());
    }

    private List<TalentDTO> getSkillDetails(CharacterSpecification characterSpecification) {
        return getLevelableSkills(characterSpecification)
                .stream()
                .map(entry -> getSkillDetail(characterSpecification, entry))
                .filter(Objects::nonNull)
                .toList();
    }

    private TalentDTO getSkillDetail(CharacterSpecification characterSpecification, int skill) {
        int level = characterSpecification.currentTalentLevels.get(skill);
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

    private List<PassiveDTO> getPassiveDetails(CharacterSpecification characterSpecification) {
        return getPassives(characterSpecification)
                .stream()
                .map(this::getPassiveDetails)
                .filter(Objects::nonNull)
                .toList();
    }

    private PassiveDTO getPassiveDetails(ProudSkillExcelConfigDataJson passive) {
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

    private List<ConstellationDTO> getConstellations(CharacterSpecification characterSpecification) {
        List<AvatarTalentExcelConfigDataJson> constellations = getSkillDepot(characterSpecification).getTalents()
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

    private Map<String, Integer> getAllAscensionItems(CharacterSpecification characterSpecification) {
        return getAscensionItems(characterSpecification, 0, getMaxAscensions(getAvatar(characterSpecification).getAvatarPromoteId())).entrySet()
                .stream()
                .filter(id -> id.getKey() != 0)
                .collect(Collectors.toMap(
                        item -> databaseService.getTranslation(getItem(item.getKey()).getNameTextMapHash()),
                        Map.Entry::getValue
                ));
    }

    private Integer getAllAscensionCosts(CharacterSpecification characterSpecification) {
        return getAscensionCost(characterSpecification, 0, getMaxAscensions(getAvatar(characterSpecification).getAvatarPromoteId()));
    }

    private Map<String, Integer> getAllTalentItems(CharacterSpecification characterSpecification) {
        return getTalents(characterSpecification).values()
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

    private Integer getAllTalentCosts(CharacterSpecification characterSpecification) {
        return getTalents(characterSpecification).values()
                .stream()
                .flatMap(map -> map.values().stream())
                .mapToInt(ProudSkillExcelConfigDataJson::getCoinCost)
                .sum();
    }

    private Integer getMaxLevel() {
        return databaseService.levelConfig
                .stream()
                .max(Comparator.comparing(AvatarLevelExcelConfigDataJson::getLevel))
                .stream()
                .mapToInt(AvatarLevelExcelConfigDataJson::getLevel)
                .findFirst()
                .orElse(-1);
    }

    private Map<String, Integer> getAllExpBooks(CharacterSpecification characterSpecification) {
        Map<String, Integer> result = new LinkedHashMap<>();

        Map<Integer, AvatarPromoteExcelConfigDataJson> ascensions = getAscensions(getAvatar(characterSpecification).getAvatarPromoteId());
        int nextStartingLevel = 1;

        for (int i = 0; i < ascensions.size(); i++) {
            AvatarPromoteExcelConfigDataJson current = ascensions.get(i);
            int currentMaxLevel = current.getUnlockMaxLevel();
            getExpBooksForLevel(characterSpecification, nextStartingLevel, currentMaxLevel).forEach((id, count) -> result.merge(id, count, Integer::sum));
            nextStartingLevel = currentMaxLevel;
        }

        return result;
    }

    private Integer getAllExpCosts(CharacterSpecification characterSpecification) {
        Map<String, Integer> expBooks = getExpBooks().keySet()
                .stream()
                .map(DatabaseWrapper::getItem)
                .collect(Collectors.toMap(
                        book -> databaseService.getTranslation(book.getNameTextMapHash()),
                        MaterialExcelConfigDataJson::getId
                ));

        return getAllExpBooks(characterSpecification).entrySet()
                .stream()
                .mapToInt(cost -> getCostForExpItem(expBooks.get(cost.getKey())) * cost.getValue())
                .sum();
    }

    private Map<String, Integer> getAllItemRequirements(CharacterSpecification characterSpecification) {
        Map<String, Integer> result = getAllAscensionItems(characterSpecification);
        getAllTalentItems(characterSpecification).forEach((id, count) -> result.merge(id, count, Integer::sum));
        getAllExpBooks(characterSpecification).forEach((id, count) -> result.merge(id, count, Integer::sum));
        return result;
    }

    private Integer getAllItemCosts(CharacterSpecification characterSpecification) {
        return getAllAscensionCosts(characterSpecification) + getAllTalentCosts(characterSpecification) + getAllExpCosts(characterSpecification);
    }

    private NameDescriptionDTO getNamecardDescription(CharacterSpecification characterSpecification) {
        MaterialExcelConfigDataJson card = getCharacterCard(characterSpecification);

        return new NameDescriptionDTO(
                databaseService.getTranslation(card.getNameTextMapHash()),
                databaseService.getTranslation(card.getDescTextMapHash())
        );
    }

    private NameDescriptionDTO getSpecialtyFoodName(CharacterSpecification characterSpecification) {
        return new NameDescriptionDTO(
                databaseService.getTranslation(getSpecialtyFood(characterSpecification).getNameTextMapHash()),
                databaseService.getTranslation(getSpecialtyFood(characterSpecification).getDescTextMapHash())
        );
    }

    private List<NameDescriptionDTO> getOutfits(CharacterSpecification characterSpecification) {
        return getCostumes(characterSpecification)
                .stream()
                .map(costume -> new NameDescriptionDTO(
                        databaseService.getTranslation(costume.getNameTextMapHash()),
                        databaseService.getTranslation(costume.getDescTextMapHash()))
                )
                .collect(Collectors.toList());
    }

    private TeaPotDTO getTeaPotDetails(CharacterSpecification characterSpecification) {
        HomeWorldFurnitureExcelConfigDataJson furniture = getFurnitureDetails(characterSpecification);

        return new TeaPotDTO(
                furniture.getFurnType()
                        .stream()
                        .map(this::getFurnitureType)
                        .collect(Collectors.toList()),
                furniture.getComfort(),
                furniture.getCost(),
                furniture.getRankLevel(),
                databaseService.getTranslation(furniture.getDescTextMapHash()),
                getPreferredFurnitureSets(characterSpecification)
                        .stream()
                        .map(set -> new NameDescriptionDTO(databaseService.getTranslation(set.getSuiteNameTextMapHash()), databaseService.getTranslation(set.getSuiteDescTextMapHash())))
                        .collect(Collectors.toList())
        );
    }

    private List<StoryDTO> getStories(CharacterSpecification characterSpecification) {
        return getFetterStories(characterSpecification)
                .stream()
                .map(entry -> {
                    String story = "";
                    if (entry.getOpenConds()
                            .stream()
                            .anyMatch(condition -> "FETTER_COND_FETTER_LEVEL".equals(condition.getCondType()))
                    ) {
                        story = databaseService.getTranslation(entry.getTips()
                                .stream()
                                .findFirst()
                                .orElse(""));
                    }

                    return new StoryDTO(
                            databaseService.getTranslation(entry.getStoryTitleTextMapHash()),
                            story,
                            databaseService.getTranslation(entry.getStoryContextTextMapHash())
                    );
                })
                .toList();
    }

    private List<StoryDTO> getQuotes(CharacterSpecification characterSpecification) {
        return getFetters(characterSpecification)
                .stream()
                .map(entry -> {
                    String story = "";
                    if (entry.getOpenConds()
                            .stream()
                            .anyMatch(condition -> "FETTER_COND_FETTER_LEVEL".equals(condition.getCondType()))
                    ) {
                        story = databaseService.getTranslation(entry.getTips()
                                .stream()
                                .findFirst()
                                .orElse(""));
                    }

                    return new StoryDTO(
                            databaseService.getTranslation(entry.getVoiceTitleTextMapHash()),
                            story,
                            databaseService.getTranslation(entry.getVoiceFileTextTextMapHash())
                    );
                })
                .toList();
    }

    private double getBaseStat(CharacterSpecification characterSpecification, double baseValue, String statType) {
        return (baseValue * getBaseStatMultiplier(characterSpecification, statType)) + getExtraBaseStats(characterSpecification, statType);
    }

    private double getExtraBaseStats(CharacterSpecification characterSpecification, String selected) {
        return getAscensions(getAvatar(characterSpecification).getAvatarPromoteId()).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() == characterSpecification.currentAscension)
                .flatMap(promotions -> promotions.getAddProps().stream())
                .filter(prop -> prop.getPropType().equals(selected))
                .mapToDouble(AddProp::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getCurveMultiplier(CharacterSpecification characterSpecification, String curveType) {
        return databaseService.avatarCurveConfig
                .stream()
                .filter(level -> level.getLevel() == characterSpecification.currentLevel)
                .flatMap(curves -> curves.getCurveInfos().stream())
                .filter(curve -> curve.getType().equals(curveType))
                .map(CurveInfo::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getBaseStatMultiplier(CharacterSpecification characterSpecification, String selected) {
        return getAvatar(characterSpecification).getPropGrowCurves()
                .stream()
                .filter(curves -> curves.getType().equals(selected))
                .map(curve -> getCurveMultiplier(characterSpecification, curve.getGrowCurve()))
                .findFirst()
                .orElse(-1.0);
    }

    private AvatarExcelConfigDataJson getAvatar(CharacterSpecification characterSpecification) {
        return databaseService.avatarConfig
                .stream()
                .filter(character -> character.getId() == characterSpecification.id)
                .findFirst()
                .orElse(null);
    }

    private FetterInfoExcelConfigDataJson getFetterInfo(CharacterSpecification characterSpecification) {
        return databaseService.fetterInfoConfig
                .stream()
                .filter(character -> character.getAvatarId() == characterSpecification.id)
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

    public int getMaxAscensions(CharacterSpecification characterSpecification) {
        return getMaxAscensions(getAvatar(characterSpecification).getAvatarPromoteId());
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

    private AvatarSkillDepotExcelConfigDataJson getSkillDepot(CharacterSpecification characterSpecification) {
        return databaseService.skillDepotConfig
                .stream()
                .filter(depot -> depot.getId() == getAvatar(characterSpecification).getSkillDepotId())
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

    private Map<String, Integer> getExpBooksForLevel(CharacterSpecification characterSpecification, int startingLevel, int targetLevel) {
        int expRequired = getExpRequired(startingLevel, targetLevel) - characterSpecification.currentExperience;

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

    private MaterialExcelConfigDataJson getCharacterCard(CharacterSpecification characterSpecification) {
        return getItem(databaseService.rewardConfig
                .stream()
                .filter(reward -> reward.getRewardId() == databaseService.fetterCharacterCardConfig
                        .stream()
                        .filter(character -> character.getAvatarId() == getAvatar(characterSpecification).getId())
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

    private MaterialExcelConfigDataJson getSpecialtyFood(CharacterSpecification characterSpecification) {
        return getItem(databaseService.cookBonusConfig
                .stream()
                .filter(id -> id.getAvatarId() == getAvatar(characterSpecification).getId())
                .flatMap(entry -> entry.getParamVec().stream())
                .filter(param -> param != 0)
                .findFirst()
                .orElse(-1));
    }

    private List<AvatarCostumeExcelConfigDataJson> getCostumes(CharacterSpecification characterSpecification) {
        return databaseService.avatarCostumeConfig
                .stream()
                .filter(id -> id.getCharacterId() == getAvatar(characterSpecification).getId())
                .collect(Collectors.toList());
    }

    private int getFurnitureId(CharacterSpecification characterSpecification) {
        return databaseService.homeWorldNPCConfig
                .stream()
                .filter(id -> id.getAvatarID() == getAvatar(characterSpecification).getId())
                .mapToInt(HomeWorldNPCExcelConfigDataJson::getFurnitureID)
                .findFirst()
                .orElse(-1);
    }

    private HomeWorldFurnitureExcelConfigDataJson getFurnitureDetails(CharacterSpecification characterSpecification) {
        return databaseService.homeWorldFurnitureConfig
                .stream()
                .filter(id -> id.getId() == getFurnitureId(characterSpecification))
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

    private List<FurnitureSuiteExcelConfigDataJson> getPreferredFurnitureSets(CharacterSpecification characterSpecification) {
        return databaseService.furnitureSuiteConfig
                .stream()
                .filter(furniture -> furniture.getFavoriteNpcExcelIdVec().contains(getAvatar(characterSpecification).getId()))
                .collect(Collectors.toList());
    }

    private List<FetterStoryExcelConfigDataJson> getFetterStories(CharacterSpecification characterSpecification) {
        return databaseService.fetterStoryConfig
                .stream()
                .filter(id -> id.getAvatarId() == getAvatar(characterSpecification).getId())
                .collect(Collectors.toList());
    }

    private List<FettersExcelConfigDataJson> getFetters(CharacterSpecification characterSpecification) {
        return databaseService.fettersConfig
                .stream()
                .filter(id -> id.getAvatarId() == getAvatar(characterSpecification).getId())
                .collect(Collectors.toList());
    }


    public void setCurrentTalentLevel(CharacterSpecification characterSpecification, int skillIndex, int skillLevel) {
        characterSpecification.currentTalentLevels.put(getLevelableSkills(characterSpecification).get(skillIndex), skillLevel);
    }

    public void setCurrentTalentLevels(CharacterSpecification characterSpecification, int skillLevel) {
        characterSpecification.currentTalentLevels = getTalents(characterSpecification).keySet()
                .stream()
                .map(AvatarSkillExcelConfigDataJson::getId)
                .collect(Collectors.toMap(
                        skillId -> skillId,
                        level -> skillLevel
                ));
    }
}
