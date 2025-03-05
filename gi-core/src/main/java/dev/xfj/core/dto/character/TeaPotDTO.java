package dev.xfj.core.dto.character;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TeaPotDTO(
        List<String> type,
        int comfort,
        int load,
        int rarity,
        String description,
        List<NameDescriptionDTO> favoriteFurniture
) {}
