package dev.xfj.character;

import dev.xfj.constants.StatType;

public record StatProperty(
        StatType statType,
        Double value
) { }
