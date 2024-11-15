package dev.xfj.weapon;

import dev.xfj.constants.StatType;
import dev.xfj.constants.WeaponStatGrowthCurve;

public record WeaponStatGrowth(
        StatType statType,
        Double startingValue,
        WeaponStatGrowthCurve statGrowthCurve
) {}
