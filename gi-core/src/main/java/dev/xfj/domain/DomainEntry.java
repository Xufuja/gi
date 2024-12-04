package dev.xfj.domain;

import dev.xfj.constants.DomainEntryConditionOperator;
import dev.xfj.constants.DomainEntryConditionType;
import dev.xfj.constants.DomainEntryType;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.dungeonentryexcelconfigdata.DungeonEntryExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class DomainEntry {
    private final DungeonEntryExcelConfigDataJson data;

    public DomainEntry(DungeonEntryExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public int getSceneId() {
        return data.getSceneId();
    }

    public int getEntryId() {
        return data.getDungeonEntryId();
    }

    public boolean isShownInHandbook() {
        return data.isIsShowInAdvHandbook();
    }

    public boolean isAvailableByDefault() {
        return data.isIsDefaultOpen();
    }

    public DomainEntryType getType() {
        return DomainEntryType.valueOf(data.getType());
    }

    public boolean isRefreshedDaily() {
        return data.isIsDailyRefresh();
    }

    public DomainEntryConditionOperator getEntryConditionOperator() {
        return DomainEntryConditionOperator.valueOf(data.getCondComb());
    }

    public List<DomainEntryCondition> getEntryConditions() {
        return data.getSatisfiedCond()
                .stream()
                .map(condition -> new DomainEntryCondition(
                        condition.getType() != null ?
                                DomainEntryConditionType.valueOf(condition.getType()) :
                                DomainEntryConditionType.NONE,
                        condition.getParam1()
                ))
                .collect(Collectors.toList());
    }

    public int getRewardDataId() {
        return data.getRewardDataId();
    }

    public List<List<Integer>> getRewardsList() {
        return data.getDescriptionCycleRewardList();
    }

    public String getIconName() {
        return data.getPicPath();
    }
}
