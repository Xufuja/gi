package dev.xfj.character;

import dev.xfj.constants.CharacterStatGrowthCurve;
import dev.xfj.constants.MathType;

public record CharacterStatCurve(
        CharacterStatGrowthCurve statGrowthCurve,
        MathType mathType,
        double value
) {}
