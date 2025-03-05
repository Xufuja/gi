package dev.xfj.core.dto.weapon;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.xfj.core.utils.KeyValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
