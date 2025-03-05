package dev.xfj.core.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StoryDTO(
        String title,
        String requirement,
        String story
) {}
