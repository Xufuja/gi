package dev.xfj.core.dto.character;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ConstellationDTO(
        int constellation,
        String name,
        String description
) {}
