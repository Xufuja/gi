package dev.xfj.core.dto.codex;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record WeaponCodexDTO(
        int id,
        String name,
        int sortFactor
) {}
