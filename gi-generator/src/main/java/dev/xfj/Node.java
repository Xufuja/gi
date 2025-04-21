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
        String result = format("%s=%s", path, type.toString());

        if (numberType != null) {
            result += format("(%s)", numberType);
        }

        return result;
    }
}
