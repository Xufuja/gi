package dev.xfj.core.dto.monster;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MonsterStatsDTO(
        int level,
        double baseHealth,
        double baseAttack,
        double baseDefense,
        MonsterResistancesDTO resistances,
        List<Integer> hpEnergyThresholdPercentage,
        List<MonsterMultiplayerStatsDTO> coopStats
) {}
