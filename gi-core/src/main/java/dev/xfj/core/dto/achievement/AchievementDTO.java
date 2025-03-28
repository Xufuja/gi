package dev.xfj.core.dto.achievement;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.xfj.core.utils.KeyValue;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AchievementDTO(
        int id,
        String name,
        String trophyName,
        String description,
        List<KeyValue> rewards,
        Integer requires,
        int sortFactor
) {}
