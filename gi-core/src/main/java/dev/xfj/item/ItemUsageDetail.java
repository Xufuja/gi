package dev.xfj.item;

import dev.xfj.constants.ItemOperation;

import java.util.List;

public record ItemUsageDetail(
        ItemOperation operation,
        List<String> parameters
) {}
