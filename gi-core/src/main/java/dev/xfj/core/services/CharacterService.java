package dev.xfj.core.services;

import dev.xfj.core.container.CharacterContainer;
import dev.xfj.core.dto.character.*;
import dev.xfj.core.dto.codex.CharacterCodexDTO;
import dev.xfj.core.utils.KeyValue;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterService {
    private final DatabaseService databaseService;
    private final ObjectProvider<CharacterContainer> characterProvider;

    @Autowired
    public CharacterService(DatabaseService databaseService, ObjectProvider<CharacterContainer> characterProvider) {
        this.databaseService = databaseService;
        this.characterProvider = characterProvider;
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
        CharacterContainer character = characterProvider.getObject();
        character.setId(characterId);
        character.setCurrentLevel(level);
        character.setCurrentExperience(experience);
        character.setCurrentAscension(ascension);

        return new CharacterProfileDTO(
                character.getId(),
                character.getName(),
                character.getTitle(),
                character.getRarity(),
                character.getBaseHealth(),
                character.getBaseAttack(),
                character.getBaseDefense(),
                character.getAscensionStat().entrySet().stream()
                        .findFirst()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .orElse(new KeyValue("", null)),
                new RequirementsDTO(character.getAscensionItems().entrySet()
                        .stream()
                        .filter(entry -> entry.getKey() != 0)
                        .map(entry -> new KeyValue(
                                databaseService.getTranslation(
                                        character.getItem(entry.getKey()).getNameTextMapHash()
                                ),
                                entry.getValue())
                        )
                        .collect(Collectors.toList()),
                        character.getAscensionCost()),
                character.getVision(),
                character.getWeaponType(),
                character.getConstellation(),
                character.getNative(),
                character.getBirthday(),
                new VoiceActorDTO(
                        character.getVA("EN"),
                        character.getVA("CHS"),
                        character.getVA("JP"),
                        character.getVA("KR")
                ),
                character.getDescription()
        );
    }

    public TalentsDTO getTalents(int characterId, int level) {
        CharacterContainer character = characterProvider.getObject();
        character.setId(characterId);
        character.setCurrentLevel(character.getMaxLevel());
        character.setCurrentAscension(character.getMaxAscensions());
        character.setCurrentTalentLevels(level);

        return new TalentsDTO(
                character.getSkillDetails(),
                character.getPassiveDetails()
        );
    }

    public List<ConstellationDTO> getConstellations(int characterId) {
        CharacterContainer character = characterProvider.getObject();
        character.setId(characterId);

        return character.getConstellations();
    }

    public MaterialsDTO getMaterials(int characterId) {
        CharacterContainer character = characterProvider.getObject();
        character.setId(characterId);

        return new MaterialsDTO(
                new RequirementsDTO(character.getAllAscensionItems().entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        character.getAllAscensionCosts()),
                new RequirementsDTO(character.getAllExpBooks().entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        character.getAllExpCosts()),
                new RequirementsDTO(character.getAllTalentItems().entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        character.getAllTalentCosts()),
                new RequirementsDTO(character.getAllItemRequirements().entrySet()
                        .stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                        character.getAllItemCosts())
        );
    }
}
