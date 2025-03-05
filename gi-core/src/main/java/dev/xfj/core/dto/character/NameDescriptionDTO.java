package dev.xfj.core.dto.character;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NameDescriptionDTO(
      String name,
      String description
) {}
