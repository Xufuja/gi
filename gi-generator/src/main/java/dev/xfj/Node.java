package dev.xfj;

import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import static java.lang.String.format;

public record Node(
        String path,
        JsonNodeType type,
        NumberType numberType
) {
    @Override
    public String toString() {
        if (numberType == null) {
            return format("%s=%s", path, type.toString());
        } else {
            return format("%s=%s(%s)", path, type.toString(), numberType);
        }
    }
}
