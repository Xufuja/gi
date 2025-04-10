package dev.xfj;

import com.fasterxml.jackson.databind.node.JsonNodeType;

public record Node(
        String path,
        JsonNodeType type
) {}
