package dev.xfj.core.dto.monster;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MonsterStatsDTO(
        int level,
        double baseHealth,
        double baseHealth2Player,
        double baseHealth3Player,
        double baseHealth4Player,
        double baseAttack,
        double baseDefense
) {}
