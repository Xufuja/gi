package dev.xfj.character;

import dev.xfj.constants.CharacterStatGrowthCurve;
import dev.xfj.constants.StatType;

public record CharacterStatGrowth(
        StatType statType,
        CharacterStatGrowthCurve statGrowthCurve
) { }
