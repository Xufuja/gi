package dev.xfj.core.dto.character;

import java.util.List;

public record TalentsDTO(
        List<TalentDTO> talents,
        List<PassiveDTO> passives
) {}
