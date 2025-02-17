package dev.xfj.core.dto.character;

import dev.xfj.core.utils.KeyValue;

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
        String description
) {}
