package dev.xfj.core.dto.codex;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemCodexDTO(
        int id,
        String name,
        String description,
        int sortFactor
) {}
