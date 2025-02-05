package dev.xfj.core.services;

import dev.xfj.core.codex.CharacterCodex;
import dev.xfj.core.container.CharacterContainer;
import dev.xfj.core.dto.character.CharacterProfileDTO;
import dev.xfj.core.dto.character.VoiceActorDTO;
import dev.xfj.core.dto.codex.CharacterCodexDTO;
import dev.xfj.core.utils.KeyValue;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterService {
    public List<CharacterCodexDTO> getCharacters() {
        return DatabaseService.getInstance().avatarCodexConfig
                .stream()
                .sorted(Comparator.comparing(AvatarCodexExcelConfigDataJson::getSortFactor))
                .map(CharacterCodex::new)
                .toList()
                .stream()
                .map(entry -> new CharacterCodexDTO(
                        entry.getId(),
                        entry.getName(),
                        entry.getReleaseTime(),
                        entry.getSortFactor())
                )
                .collect(Collectors.toList());
    }

    public CharacterProfileDTO getCharacter(int characterId, int level, int experience, int ascension) {
        CharacterContainer character = new CharacterContainer(characterId, level, experience, ascension);

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
}
