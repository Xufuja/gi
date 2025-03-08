package dev.xfj.core.dto.character;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CharacterCodexDTO(
        int id,
        String name,
        LocalDateTime releaseTime,
        int sortFactor
) {}
