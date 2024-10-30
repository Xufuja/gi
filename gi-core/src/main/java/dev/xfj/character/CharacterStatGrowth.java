package dev.xfj.character;

import dev.xfj.constants.CharacterStatGrowthCurve;
import dev.xfj.constants.StatGrowthType;

public record CharacterStatGrowth(
        StatGrowthType statGrowthType,
        CharacterStatGrowthCurve statGrowthCurve
) { }
