package dev.xfj.domain;

import dev.xfj.constants.*;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.dungeonexcelconfigdata.DungeonExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class Domain {
    private final DungeonExcelConfigDataJson data;

    public Domain(DungeonExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDisplayName() {
        return Database.getInstance().getTranslation(data.getDisplayNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public DomainType getType() {
        return DomainType.valueOf(data.getType());
    }

    public DomainSubType getSubType() {
        return DomainSubType.valueOf(data.getSubType());
    }

    public DomainPlayType getPlayType() {
        return DomainPlayType.valueOf(data.getPlayType());
    }

    public int getSceneId() {
        return data.getSceneId();
    }

    public DomainParticipant getParticipants() {
        return DomainParticipant.valueOf(data.getInvolveType());
    }

    public int getDisplayLevel() {
        return data.getShowLevel();
    }

    public int getMinimumLevel() {
        return data.getLimitLevel();
    }

    public int getMaximumRevives() {
        return data.getReviveMaxCount();
    }

    public int getDailyLimit() {
        return data.getDayEnterCount();
    }

    public int getDomainFinalizationTime() {
        return data.getSettleCountdownTime();
    }

    public int getFailureDomainFinalizationTime() {
        return data.getFailSettleCountdownTime();
    }

    public int getAbandonDomainFinalizationTime() {
        return data.getQuitSettleCountdownTime();
    }

    public List<DomainFinalization> getFinalizationActions() {
        return data.getSettleShows().stream().map(DomainFinalization::valueOf).collect(Collectors.toList());
    }

    public boolean isRestartAllowed() {
        return !data.isForbiddenRestart();
    }

    public DomainFinalizationUI getFinalizationUI() {
        return DomainFinalizationUI.valueOf(data.getSettleUIType());
    }

    public List<String> getRecommendedElements() {
        return data.getRecommendElementTypes().stream().distinct().collect(Collectors.toList());
    }

    public List<DomainTip> getTips() {
        return data.getRecommendTips().stream().map(DomainTip::valueOf).collect(Collectors.toList());
    }

    public List<Integer> getPreviewMonsterIds() {
        return data.getPreviewMonsterList();
    }

    public int getCityId() {
        return data.getCityID();
    }

    public DomainState getDomainState() {
        return DomainState.valueOf(data.getStateType());
    }

    public DomainLimitation getDomainLimitation() {
        return DomainLimitation.valueOf(data.getMgggicjglhn());
    }

    public String getIconName() {
        return data.getEntryPicPath();
    }

}
