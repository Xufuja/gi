package dev.xfj.core.dto;

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
