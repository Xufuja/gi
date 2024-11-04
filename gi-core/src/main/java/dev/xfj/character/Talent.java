package dev.xfj.character;

import dev.xfj.constants.DragType;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.avatarskillexcelconfigdata.AvatarSkillExcelConfigDataJson;

public class Talent {
    private final AvatarSkillExcelConfigDataJson data;

    public Talent(AvatarSkillExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public String getAbilityName() {
        return data.getAbilityName();
    }

    public String getElementalTyping() {
        return data.getCostElemType();
    }

    public TalentElementCost getElementalRequirement() {
        return new TalentElementCost(data.getCostElemType(), data.getCostElemVal());
    }

    public int getCooldownTime() {
        return data.getCdTime();
    }

    public int getStaminaCost() {
        return data.getCostStamina();
    }

    public int getMaxChargeCount() {
        return data.getMaxChargeNum();
    }

    public int getPassiveGroupId() {
        return data.getProudSkillGroupId();
    }

    public TalentActivationMonitor getActivationMonitoring() {
        return TalentActivationMonitor.valueOf(data.getNeedMonitor());
    }

    public int getCooldownSlots() {
        return data.getCdSlot();
    }

    public int getTriggerId() {
        return data.getTriggerID();
    }

    public DragType getDragType() {
        return DragType.valueOf(data.getDragType());
    }

    public boolean isRanged() {
        return data.isIsRanged();
    }

    public boolean isLockedByDefault() {
        return data.isDefaultLocked();
    }

    public boolean requiresStoring() {
        return data.isNeedStore();
    }

    public boolean ignoreCooldownMinusRatio() {
        return data.isIgnoreCDMinusRatio();
    }

    public boolean canForceActivation() {
        return data.isForceCanDoSkill();
    }

    public boolean locksCamera() {
        return data.isIsAttackCameraLock();
    }

    public boolean showArrowIcon() {
        return data.isShowIconArrow();
    }

    public String getIconName() {
        return data.getSkillIcon();
    }

    public String getBuffIconName() {
        return data.getBuffIcon();
    }
}
