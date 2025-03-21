package dev.xfj.core.dto.monster;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MonsterResistancesDTO(
        double pyro,
        double dendro,
        double hydro,
        double electro,
        double anemo,
        double cryo,
        double geo,
        double physical
) {}
