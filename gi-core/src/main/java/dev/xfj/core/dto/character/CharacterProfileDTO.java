package dev.xfj.core.dto.character;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.xfj.core.dto.common.RequirementsDTO;
import dev.xfj.core.utils.KeyValue;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CharacterProfileDTO(
        int id,
        String name,
        String title,
        int rarity,
        double baseHealth,
        double baseAttack,
        double baseDefense,
        KeyValue ascensionStat,
        RequirementsDTO nextAscension,
        String element,
        String weapon,
        String constellation,
        String affiliation,
        String birthday,
        VoiceActorDTO voiceActor,
        String description,
        NameDescriptionDTO namecard,
        NameDescriptionDTO specialtyFood,
        List<NameDescriptionDTO> costumes
) {}
