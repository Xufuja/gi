package dev.xfj.core.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.xfj.core.utils.KeyValue;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RequirementsDTO(
        List<KeyValue> requiredItems,
        int requiredMora
) {}
