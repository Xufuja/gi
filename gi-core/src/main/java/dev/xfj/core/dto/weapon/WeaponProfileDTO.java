package dev.xfj.core.dto.weapon;

import dev.xfj.core.utils.KeyValue;

public record WeaponProfileDTO(
        int id,
        String name,
        String weaponType,
        double baseAttack,
        KeyValue ascensionStat,
        int rarity,
        String effect,
        String description
) {}
