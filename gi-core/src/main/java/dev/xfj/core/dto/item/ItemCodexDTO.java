package dev.xfj.core.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemCodexDTO(
        int id,
        String name,
        String description,
        int sortFactor
) {}
