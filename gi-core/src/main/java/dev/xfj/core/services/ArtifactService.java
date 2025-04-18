package dev.xfj.core.services;

import dev.xfj.core.dto.artifact.*;
import dev.xfj.core.dto.character.NameDescriptionDTO;
import dev.xfj.core.dto.common.StoryDTO;
import dev.xfj.core.specification.ArtifactSpecification;
import dev.xfj.generated.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.generated.localizationexcelconfigdata.LocalizationExcelConfigDataJson;
import dev.xfj.generated.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;
import dev.xfj.generated.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
import dev.xfj.generated.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.generated.reliquarylevelexcelconfigdata.AddProp;
import dev.xfj.generated.reliquarylevelexcelconfigdata.ReliquaryLevelExcelConfigDataJson;
import dev.xfj.generated.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;
import dev.xfj.generated.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;

@Service
public class ArtifactService {
    private final DatabaseService databaseService;

    @Autowired
    public ArtifactService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<ArtifactSetCodexDTO> getArtifactSets() {
        return databaseService.reliquaryCodexConfig
                .stream()
                .sorted(Comparator.comparing(ReliquaryCodexExcelConfigDataJson::getSortOrder))
                .map(data -> new ArtifactSetCodexDTO(
                        data.getSuitId(),
                        data.getLevel(),
                        filter(Map.of(data.getCupId(), getName(getArtifact(data.getCupId())))),
                        filter(Map.of(data.getLeatherId(), getName(getArtifact(data.getLeatherId())))),
                        filter(Map.of(data.getCapId(), getName(getArtifact(data.getCapId())))),
                        filter(Map.of(data.getFlowerId(), getName(getArtifact(data.getFlowerId())))),
                        filter(Map.of(data.getSandId(), getName(getArtifact(data.getSandId())))),
                        data.getSortOrder())
                )
                .collect(Collectors.toList());
    }

    public ArtifactProfileDTO getArtifact(int artifactId, int level, int experience) {
        ArtifactSpecification artifact = new ArtifactSpecification();
        artifact.id = artifactId;
        artifact.currentLevel = level;
        artifact.currentExperience = experience;

        return new ArtifactProfileDTO(
                artifact.id,
                getSetName(artifact),
                getArtifactType(artifact),
                getName(artifact),
                getRarity(artifact),
                getSetEffect(artifact).entrySet()
                        .stream()
                        .map(entry -> new NameDescriptionDTO(format("%s-Pieces", entry.getKey()), entry.getValue()))
                        .collect(Collectors.toList()),
                getDescription(artifact)
        );
    }

    public ArtifactStatsDTO getStats(int artifactId) {
        ArtifactSpecification artifact = new ArtifactSpecification();
        artifact.id = artifactId;

        return new ArtifactStatsDTO(
                getMainStats(artifact).entrySet()
                        .stream()
                        .map(entry -> new ArtifactStatEntryDTO(
                                        entry.getKey(),
                                        "Per Level",
                                        IntStream.range(0, entry.getValue().size())
                                                .boxed()
                                                .collect(Collectors.toMap(
                                                                i -> i,
                                                                i -> entry.getValue().get(i)
                                                        )
                                                )
                                )
                        )
                        .collect(Collectors.toList()),
                getSubStats(artifact).entrySet()
                        .stream()
                        .map(entry -> new ArtifactStatEntryDTO(
                                        entry.getKey(),
                                        "Possible Rolls",
                                        IntStream.range(0, entry.getValue().size())
                                                .boxed()
                                                .collect(Collectors.toMap(
                                                                i -> i,
                                                                i -> entry.getValue().get(i)
                                                        )
                                                )
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    public List<StoryDTO> getStories(int artifactSetId) {
        List<String> keys = databaseService.readableMap.keySet()
                .stream()
                .filter(key -> key.contains(String.valueOf(artifactSetId)))
                .toList();

        return databaseService.readableMap.entrySet()
                .stream()
                .filter(entry -> keys.contains(entry.getKey()))
                .filter(entry -> !entry.getValue().isBlank())
                .map(entry -> new StoryDTO(entry.getKey(), null, entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<StoryDTO> getStory(int artifactId) {
        ArtifactSpecification artifact = new ArtifactSpecification();
        artifact.id = artifactId;

        String name = databaseService.localizationConfig
                .stream()
                .filter(
                        entry -> String.valueOf(entry.getId())
                                .endsWith(String.valueOf(getArtifact(artifactId).getStoryId()).substring(1))
                )
                .map(this::pathMapper)
                .map(entry -> entry.split(databaseService.language + "/")[1])
                .findFirst()
                .orElse(null);

        return List.of(new StoryDTO(name, null, databaseService.readableMap.get(name)));
    }

    private String pathMapper(LocalizationExcelConfigDataJson locale) {
        return switch (databaseService.language) {
            case "CHS" -> locale.getScPath();
            case "CHT" -> locale.getTcPath();
            default -> {
                try {
                    yield (String) LocalizationExcelConfigDataJson.class.getMethod(
                                    format(
                                            "get%s%sPath",
                                            databaseService.language.charAt(0),
                                            databaseService.language.substring(1).toLowerCase()
                                    )
                            )
                            .invoke(locale);
                } catch (Exception e) {
                    yield locale.getDefaultPath();
                }
            }
        };
    }

    private ReliquaryExcelConfigDataJson getArtifact(int id) {
        return databaseService.reliquaryConfig
                .stream()
                .filter(artifact -> artifact.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private String getName(ReliquaryExcelConfigDataJson artifact) {
        if (artifact != null) {
            return databaseService.getTranslation(artifact.getNameTextMapHash());
        } else {
            return "";
        }
    }

    private ArtifactEntryDTO filter(Map<Integer, String> artifact) {
        if (artifact.containsKey(0)) {
            return null;
        }

        return artifact.entrySet()
                .stream()
                .map(entry -> new ArtifactEntryDTO(entry.getKey(), entry.getValue()))
                .findFirst()
                .orElse(null);
    }

    private String getSetName(ArtifactSpecification artifactSpecification) {
        return getAffixes(getArtifactSet(artifactSpecification).getEquipAffixId()).values()
                .stream()
                .findFirst()
                .map(entry -> databaseService.getTranslation(entry.getNameTextMapHash()))
                .orElse(null);
    }

    private String getName(ArtifactSpecification artifactSpecification) {
        return databaseService.getTranslation(getArtifact(artifactSpecification).getNameTextMapHash());
    }

    private String getArtifactType(ArtifactSpecification artifactSpecification) {
        return databaseService.getManualMappedText(getArtifact(artifactSpecification).getEquipType());
    }

    private Integer getRarity(ArtifactSpecification artifactSpecification) {
        return getArtifact(artifactSpecification).getRankLevel();
    }

    private Map<Integer, String> getSetEffect(ArtifactSpecification artifactSpecification) {
        ReliquarySetExcelConfigDataJson set = getArtifactSet(artifactSpecification);

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

    private String getDescription(ArtifactSpecification artifactSpecification) {
        return databaseService.getTranslation(getArtifact(artifactSpecification).getDescTextMapHash());
    }

    private Map<String, List<Double>> getMainStats(ArtifactSpecification artifactSpecification) {
        return getPossibleMainStats(artifactSpecification)
                .stream()
                .map(ReliquaryMainPropExcelConfigDataJson::getPropType)
                .collect(Collectors.toMap(
                                entry -> entry.contains("PERCENT") ? databaseService.getManualMappedText(entry) + " %" : databaseService.getManualMappedText(entry),
                                entry -> new ArrayList<>(getLevelData(getRarity(artifactSpecification), entry).values())
                        )
                );
    }

    private Map<String, List<Double>> getSubStats(ArtifactSpecification artifactSpecification) {
        return getPossibleSubStats(artifactSpecification).values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        entry -> entry.getPropType().contains("PERCENT") ? databaseService.getManualMappedText(entry.getPropType()) + " %" : databaseService.getManualMappedText(entry.getPropType()),
                        Collectors.mapping(
                                ReliquaryAffixExcelConfigDataJson::getPropValue,
                                Collectors.toList()
                        )
                ));
    }

    private int getExpNeededForNextLevel(ArtifactSpecification artifactSpecification) {
        if (artifactSpecification.currentLevel <= getMaxLevel(getRarity(artifactSpecification))) {
            return getExpRequired(getRarity(artifactSpecification), artifactSpecification.currentLevel, artifactSpecification.currentLevel + 1) - artifactSpecification.currentExperience;
        }

        return 0;
    }

    private List<Integer> getArtifactsInSet(ArtifactSpecification artifactSpecification) {
        return getArtifactSet(artifactSpecification).getContainsList();
    }

    private ReliquaryExcelConfigDataJson getArtifact(ArtifactSpecification artifactSpecification) {
        return databaseService.reliquaryConfig
                .stream()
                .filter(artifact -> artifact.getId() == artifactSpecification.id)
                .findFirst()
                .orElse(null);
    }

    private ReliquarySetExcelConfigDataJson getArtifactSet(ArtifactSpecification artifactSpecification) {
        return databaseService.reliquarySetConfig
                .stream()
                .filter(set -> set.getSetId() == getArtifact(artifactSpecification).getSetId())
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

    private List<ReliquaryMainPropExcelConfigDataJson> getPossibleMainStats(ArtifactSpecification artifactSpecification) {
        return databaseService.reliquaryMainPropConfig
                .stream()
                .filter(stat -> getArtifact(artifactSpecification.id).getMainPropDepotId() == stat.getPropDepotId())
                .filter(stat -> !getArtifact(artifactSpecification.id).getEquipType().equals("EQUIP_BRACER") && !stat.getPropType().equals("FIGHT_PROP_HP"))
                .filter(stat -> !getArtifact(artifactSpecification.id).getEquipType().equals("EQUIP_NECKLACE") && !stat.getPropType().equals("FIGHT_PROP_ATTACK"))
                .filter(stat -> !stat.getPropType().equals("FIGHT_PROP_DEFENSE"))
                .collect(Collectors.toList());
    }

    private Map<Integer, Double> getLevelData(int rarity, String stat) {
        return databaseService.reliquaryLevelConfig
                .stream()
                .filter(entry -> entry.getRank() == rarity)
                .collect(Collectors.toMap(
                                entry -> entry.getLevel() - 1,
                                entry -> entry.getAddProps()
                                        .stream()
                                        .filter(prop -> prop.getPropType().equals(stat))
                                        .map(AddProp::getValue)
                                        .findFirst()
                                        .orElse(-1.0)
                        )
                );
    }

    private Map<Integer, List<ReliquaryAffixExcelConfigDataJson>> getPossibleSubStats(ArtifactSpecification artifactSpecification) {
        return databaseService.reliquaryAffixConfig
                .stream()
                .filter(entry -> entry.getDepotId() == getArtifact(artifactSpecification).getAppendPropDepotId())
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
