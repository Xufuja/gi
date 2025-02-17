package dev.xfj.core.dto.character;

public record MaterialsDTO(
        RequirementsDTO levels,
        RequirementsDTO ascensions,
        RequirementsDTO talents,
        RequirementsDTO combined
) {}
