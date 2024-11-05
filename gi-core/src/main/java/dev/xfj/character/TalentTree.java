package dev.xfj.character;

import dev.xfj.constants.ArkheType;
import dev.xfj.jsonschema2pojo.avatarskilldepotexcelconfigdata.AvatarSkillDepotExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class TalentTree {
    private final AvatarSkillDepotExcelConfigDataJson data;

    public TalentTree(AvatarSkillDepotExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public int getBurstId() {
        return data.getEnergySkill();
    }

    public List<Integer> getTalentIds() {
        return data.getSkills();
    }

    public List<Integer> getBasicTalentIds() {
        return data.getSubSkills();
    }

    public List<TalentPassive> getPassives() {
        return data.getInherentProudSkillOpens()
                .stream()
                .map(passive -> new TalentPassive(passive.getProudSkillGroupId(), passive.getNeedAvatarPromoteLevel()))
                .collect(Collectors.toList());
    }

    public int getAlternateAttackModeId() {
        return data.getAttackModeSkill();
    }

    public String getTalentTreeGroup() {
        return data.getSkillDepotAbilityGroup();
    }

    public ArkheType getArkhe() {
        return data.getLenbicofdeo() != null ? ArkheType.valueOf(data.getLenbicofdeo().toUpperCase()) : ArkheType.NONE;
    }
}
