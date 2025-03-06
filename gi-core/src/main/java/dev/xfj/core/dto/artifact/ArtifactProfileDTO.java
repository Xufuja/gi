package dev.xfj.core.dto.artifact;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.xfj.core.dto.character.NameDescriptionDTO;
import dev.xfj.core.utils.KeyValue;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtifactProfileDTO(
        int id,
        String setName,
        String artifactType,
        String name,
        int rarity,
        List<NameDescriptionDTO> effects,
        String description
) {}
