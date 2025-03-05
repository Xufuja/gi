package dev.xfj.core.dto.codex;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtifactSetCodexDTO(
        int id,
        int rarity,
        ArtifactEntryDTO goblet,
        ArtifactEntryDTO feather,
        ArtifactEntryDTO circlet,
        ArtifactEntryDTO flower,
        ArtifactEntryDTO sands,
        int sortFactor
) {}
