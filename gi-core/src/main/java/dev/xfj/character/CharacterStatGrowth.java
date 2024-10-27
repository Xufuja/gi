package dev.xfj.character;

import dev.xfj.constants.CharacterStatGrowthCurve;
import dev.xfj.constants.CharacterStatGrowthType;

public record CharacterStatGrowth(
        CharacterStatGrowthType statGrowthType,
        CharacterStatGrowthCurve statGrowthCurve
) { }
