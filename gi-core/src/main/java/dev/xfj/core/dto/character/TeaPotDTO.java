package dev.xfj.core.dto.character;

import java.util.List;

public record TeaPotDTO(
        List<String> type,
        int comfort,
        int load,
        int rarity,
        String description,
        List<NameDescriptionDTO> favoriteFurniture
) {}
