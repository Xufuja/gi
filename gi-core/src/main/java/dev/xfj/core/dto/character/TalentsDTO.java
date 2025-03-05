package dev.xfj.core.dto.character;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TalentsDTO(
        List<TalentDTO> talents,
        List<PassiveDTO> passives
) {}
