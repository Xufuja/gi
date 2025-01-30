package dev.xfj.core.dto;

import java.time.LocalDateTime;

public record CharacterCodexDTO(
        int id,
        String name,
        LocalDateTime releaseTime,
        int sortFactor
) {}
