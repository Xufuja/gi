package dev.xfj.core.dto.codex;

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
