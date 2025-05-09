package dev.xfj.core.dto.achievement;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AchievementCodexDTO(
        int id,
        String category,
        List<AchievementDTO> achievements,
        int sortFactor
) {}
