package dev.xfj.core.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemProfileDTO(
        int id,
        String name,
        String itemType,
        int rarity,
        String description,
        List<String> source
) {}
