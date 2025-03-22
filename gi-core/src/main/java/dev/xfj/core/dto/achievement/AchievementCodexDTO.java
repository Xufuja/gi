package dev.xfj.core.dto.achievement;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AchievementCodexDTO(
        int id,
        String name,
        int sortFactor
) {}
