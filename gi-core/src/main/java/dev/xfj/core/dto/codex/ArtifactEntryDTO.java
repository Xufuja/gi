package dev.xfj.core.dto.codex;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtifactEntryDTO(
        int id,
        String name
) {
}
