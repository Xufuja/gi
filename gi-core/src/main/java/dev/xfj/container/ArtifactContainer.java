package dev.xfj.container;

import dev.xfj.database.*;
import dev.xfj.jsonschema2pojo.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarylevelexcelconfigdata.AddProp;
import dev.xfj.jsonschema2pojo.reliquarylevelexcelconfigdata.ReliquaryLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArtifactContainer {
    private int id;
    private int currentLevel;
    private int currentExperience;

    public ArtifactContainer(int id) {
        this(id, 1, 0);
    }

    public ArtifactContainer(int id, int currentLevel, int currentExperience) {
        this.id = id;
        this.currentLevel = currentLevel;
        this.currentExperience = currentExperience;
    }

    public int getId() {
        return id;
    }

    public String getSetName() {
        return getAffixes(getArtifactSet().getEquipAffixId()).values()
                .stream()
                .findFirst()
                .map(entry -> Database.getInstance().getTranslation(entry.getNameTextMapHash()))
                .orElse(null);
    }

    public String getName() {
        return Database.getInstance().getTranslation(getArtifact().getNameTextMapHash());
    }

    public String getArtifactType() {
        return getManualMappedText(getArtifact().getEquipType());
    }

    public int getRarity() {
        return getArtifact().getRankLevel();
    }

    public Map<Integer, String> getSetEffect() {
        ReliquarySetExcelConfigDataJson set = getArtifactSet();

        List<String> bonuses = getAffixes(set.getEquipAffixId()).values()
                .stream()
                .map(entry -> Database.getInstance().getTranslation(entry.getDescTextMapHash()))
                .toList();

        return IntStream.range(0, set.getSetNeedNum().size())
                .boxed()
                .collect(Collectors.toMap(
                        set.getSetNeedNum()::get,
                        bonuses::get
                ));
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(getArtifact().getDescTextMapHash());
    }

    public Map<String, Map<Integer, Double>> getMainStats() {
        return getPossibleMainStats()
                .stream()
                .map(ReliquaryMainPropExcelConfigDataJson::getPropType)
                .collect(Collectors.toMap(
                                this::getManualMappedText,
                                entry -> getLevelData(getRarity(), entry)
                        )
                );
    }

    public Map<String, List<Double>> getSubStats() {
        return getPossibleSubStats().values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        entry -> getManualMappedText(entry.getPropType()),
                        Collectors.mapping(
                                ReliquaryAffixExcelConfigDataJson::getPropValue,
                                Collectors.toList()
                        )
                ));
    }

    public int getExpNeededForNextLevel() {
        if (currentLevel <= getMaxLevel(getRarity())) {
            return getExpRequired(getRarity(), currentLevel, currentLevel + 1);
        }

        return 0;
    }

    public List<Integer> getArtifactsInSet() {
        return getArtifactSet().getContainsList();
    }

    private ReliquaryExcelConfigDataJson getArtifact() {
        return ReliquaryData.getInstance().reliquaryConfig
                .stream()
                .filter(artifact -> artifact.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private String getManualMappedText(String id) {
        return TextMapData.getInstance().manualTextMapConfig
                .stream()
                .filter(text -> id.equals(text.getTextMapId()))
                .map(map -> Database.getInstance().getTranslation(map.getTextMapContentTextMapHash()))
                .findAny()
                .orElse(null);
    }

    private ReliquarySetExcelConfigDataJson getArtifactSet() {
        return ReliquarySetData.getInstance().reliquarySetConfig
                .stream()
                .filter(set -> set.getSetId() == getArtifact().getSetId())
                .findAny()
                .orElse(null);
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

    private List<ReliquaryMainPropExcelConfigDataJson> getPossibleMainStats() {
        return ReliquaryData.getInstance().reliquaryMainPropConfig
                .stream()
                .filter(stat -> getArtifact().getMainPropDepotId() == stat.getPropDepotId())
                .collect(Collectors.toList());
    }

    private Map<Integer, Double> getLevelData(int rarity, String stat) {
        return ReliquaryData.getInstance().reliquaryLevelConfig
                .stream()
                .filter(entry -> entry.getRank() == rarity)
                .collect(Collectors.toMap(
                                ReliquaryLevelExcelConfigDataJson::getLevel,
                                entry -> entry.getAddProps()
                                        .stream()
                                        .filter(prop -> prop.getPropType().equals(stat))
                                        .map(AddProp::getValue)
                                        .findFirst()
                                        .orElse(-1.0)
                        )
                );
    }

    private Map<Integer, List<ReliquaryAffixExcelConfigDataJson>> getPossibleSubStats() {
        return ReliquaryData.getInstance().reliquaryAffixConfig
                .stream()
                .filter(entry -> entry.getDepotId() == getArtifact().getAppendPropDepotId())
                .collect(Collectors.groupingBy(ReliquaryAffixExcelConfigDataJson::getGroupId));
    }

    private int getMaxLevel(int rarity) {
        return ReliquaryData.getInstance().reliquaryLevelConfig
                .stream()
                .filter(entry -> entry.getRank() == rarity)
                .max(Comparator.comparing(ReliquaryLevelExcelConfigDataJson::getLevel))
                .stream()
                .mapToInt(ReliquaryLevelExcelConfigDataJson::getLevel)
                .findFirst()
                .orElse(-1);
    }

    private int getExpRequired(int rarity, int startingLevel, int targetLevel) {
        return ReliquaryData.getInstance().reliquaryLevelConfig
                .stream()
                .filter(entry -> entry.getRank() == rarity)
                .filter(level -> level.getLevel() >= startingLevel)
                .filter(level -> level.getLevel() < targetLevel)
                .mapToInt(ReliquaryLevelExcelConfigDataJson::getExp)
                .sum();
    }
}
