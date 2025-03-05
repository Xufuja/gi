package dev.xfj.core.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MaterialsDTO(
        RequirementsDTO levels,
        RequirementsDTO ascensions,
        RequirementsDTO talents,
        RequirementsDTO combined
) {}
