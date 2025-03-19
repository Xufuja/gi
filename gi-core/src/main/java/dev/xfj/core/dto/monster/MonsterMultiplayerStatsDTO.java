package dev.xfj.core.dto.monster;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.xfj.core.constants.MultiplayerCount;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MonsterMultiplayerStatsDTO(
        MultiplayerCount players,
        double baseHealth,
        double baseAttack
) {}