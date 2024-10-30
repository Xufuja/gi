package dev.xfj.weapon;

import dev.xfj.constants.StatGrowthType;
import dev.xfj.constants.WeaponStatGrowthCurve;

public record WeaponStatGrowth(
        StatGrowthType statGrowthType,
        Double startingValue,
        WeaponStatGrowthCurve statGrowthCurve
) {}
