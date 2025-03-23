package dev.xfj.core.dto.achievement;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AchievementDTO(
        int id,
        String name,
        String description,
        int sortFactor
) {}
