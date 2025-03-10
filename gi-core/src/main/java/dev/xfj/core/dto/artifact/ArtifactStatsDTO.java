package dev.xfj.core.dto.artifact;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtifactStatsDTO(
        List<ArtifactStatEntryDTO> mainStats,
        List<ArtifactStatEntryDTO> subStats
) {}
