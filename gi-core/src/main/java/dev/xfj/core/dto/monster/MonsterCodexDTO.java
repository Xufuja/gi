package dev.xfj.core.dto.monster;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MonsterCodexDTO(
        int id,
        String name,
        int sortFactor
) {}
