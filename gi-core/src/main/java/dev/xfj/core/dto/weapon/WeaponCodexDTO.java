package dev.xfj.core.dto.weapon;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record WeaponCodexDTO(
        int id,
        String name,
        int sortFactor
) {}
