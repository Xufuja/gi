package dev.xfj.core.logic;

import dev.xfj.core.services.DatabaseService;
import dev.xfj.core.services.DatabaseWrapper;
import dev.xfj.jsonschema2pojo.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarylevelexcelconfigdata.AddProp;
import dev.xfj.jsonschema2pojo.reliquarylevelexcelconfigdata.ReliquaryLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.xfj.core.services.DatabaseWrapper.getManualMappedText;

@Component
@Scope("prototype")
public class ArtifactLogic {
    private final DatabaseService databaseService;
    private int id;
    private int currentLevel;
    private int currentExperience;

    @Autowired
    public ArtifactLogic(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public Integer getId() {
        return id;
    }

    public String getSetName() {
        return getAffixes(getArtifactSet().getEquipAffixId()).values()
                .stream()
                .findFirst()
                .map(entry -> databaseService.getTranslation(entry.getNameTextMapHash()))
                .orElse(null);
    }

    public String getName() {
        return databaseService.getTranslation(getArtifact().getNameTextMapHash());
    }

    public String getArtifactType() {
        return getManualMappedText(getArtifact().getEquipType());
    }

    public Integer getRarity() {
        return getArtifact().getRankLevel();
    }

    public Map<Integer, String> getSetEffect() {
        ReliquarySetExcelConfigDataJson set = getArtifactSet();

        List<String> bonuses = getAffixes(set.getEquipAffixId()).values()
                .stream()
                .map(entry -> databaseService.getTranslation(entry.getDescTextMapHash()))
                .toList();

        return IntStream.range(0, set.getSetNeedNum().size())
                .boxed()
                .collect(Collectors.toMap(
                        set.getSetNeedNum()::get,
                        bonuses::get
                ));
    }

    public String getDescription() {
        return databaseService.getTranslation(getArtifact().getDescTextMapHash());
    }

    public Map<String, Map<Integer, Double>> getMainStats() {
        return getPossibleMainStats()
                .stream()
                .map(ReliquaryMainPropExcelConfigDataJson::getPropType)
                .collect(Collectors.toMap(
                                DatabaseWrapper::getManualMappedText,
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
            return getExpRequired(getRarity(), currentLevel, currentLevel + 1) - currentExperience;
        }

        return 0;
    }

    public List<Integer> getArtifactsInSet() {
        return getArtifactSet().getContainsList();
    }

    private ReliquaryExcelConfigDataJson getArtifact() {
        return databaseService.reliquaryConfig
                .stream()
                .filter(artifact -> artifact.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private ReliquarySetExcelConfigDataJson getArtifactSet() {
        return databaseService.reliquarySetConfig
                .stream()
                .filter(set -> set.getSetId() == getArtifact().getSetId())
                .findAny()
                .orElse(null);
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

    private List<ReliquaryMainPropExcelConfigDataJson> getPossibleMainStats() {
        return databaseService.reliquaryMainPropConfig
                .stream()
                .filter(stat -> getArtifact().getMainPropDepotId() == stat.getPropDepotId())
                .collect(Collectors.toList());
    }

    private Map<Integer, Double> getLevelData(int rarity, String stat) {
        return databaseService.reliquaryLevelConfig
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
        return databaseService.reliquaryAffixConfig
                .stream()
                .filter(entry -> entry.getDepotId() == getArtifact().getAppendPropDepotId())
                .collect(Collectors.groupingBy(ReliquaryAffixExcelConfigDataJson::getGroupId));
    }

    private int getMaxLevel(int rarity) {
        return databaseService.reliquaryLevelConfig
                .stream()
                .filter(entry -> entry.getRank() == rarity)
                .max(Comparator.comparing(ReliquaryLevelExcelConfigDataJson::getLevel))
                .stream()
                .mapToInt(ReliquaryLevelExcelConfigDataJson::getLevel)
                .findFirst()
                .orElse(-1);
    }

    private int getExpRequired(int rarity, int startingLevel, int targetLevel) {
        return databaseService.reliquaryLevelConfig
                .stream()
                .filter(entry -> entry.getRank() == rarity)
                .filter(level -> level.getLevel() >= startingLevel)
                .filter(level -> level.getLevel() < targetLevel)
                .mapToInt(ReliquaryLevelExcelConfigDataJson::getExp)
                .sum();
    }
}
