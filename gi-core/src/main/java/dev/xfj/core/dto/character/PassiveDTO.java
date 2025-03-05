package dev.xfj.core.dto.character;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.xfj.core.utils.KeyValue;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PassiveDTO(
        int id,
        String name,
        String description,
        List<KeyValue> attributes
) {}
