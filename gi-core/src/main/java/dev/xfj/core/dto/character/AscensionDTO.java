package dev.xfj.core.dto.character;

import dev.xfj.core.utils.KeyValue;

import java.util.List;

public record AscensionDTO(
        List<KeyValue> requiredItems,
        int requiredMora
) {}
