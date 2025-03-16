package dev.xfj.core.dto.monster;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MonsterProfileDTO(
        int id,
        String name,
        String title,
        String description
) {}
