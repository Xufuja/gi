package dev.xfj;

import com.fasterxml.jackson.databind.node.JsonNodeType;

import static java.lang.String.format;

public record Node(
        String path,
        JsonNodeType type
) {
    @Override
    public String toString() {
        return format("%s=%s", path, type.toString());
    }
}
