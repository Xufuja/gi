package dev.xfj.core.dto.character;

import dev.xfj.core.utils.KeyValue;

import java.util.List;

public record TalentDTO(
        int id,
        String name,
        String description,
        int level,
        List<KeyValue> attributes
) {
}
