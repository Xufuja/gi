package dev.xfj.domain;

import dev.xfj.constants.DomainEntryConditionType;

public record DomainEntryCondition(
        DomainEntryConditionType conditionType,
        int parameter
) {}
