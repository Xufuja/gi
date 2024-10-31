package dev.xfj.character;

import dev.xfj.constants.*;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class Character {
    private final AvatarExcelConfigDataJson data;

    public Character(AvatarExcelConfigDataJson data) {
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

    public int getRarity() {
        return CharacterRarity.valueOf(data.getQualityType()).getStarValue();
    }

    public CharacterBodyType getBodyType() {
        return CharacterBodyType.valueOf(data.getBodyType());
    }

    public WeaponType getWeaponType() {
        return WeaponType.valueOf(data.getWeaponType());
    }

    public boolean isRanged() {
        return data.isIsRangeAttack();
    }

    public int getStartingWeaponId() {
        return data.getInitialWeapon();
    }

    public int getDefaultSkillTreeId() {
        return data.getSkillDepotId();
    }

    public List<Integer> getAlternateSkillTreeIds() {
        return data.getCandSkillDepotIds();
    }

    public int getAscensionId() {
        return data.getAvatarPromoteId();
    }

    public List<Integer> getAscensionRewardLevels() {
        return data.getAvatarPromoteRewardLevelList();
    }

    public List<Integer> getAscensionRewardIds() {
        return data.getAvatarPromoteRewardIdList();
    }

    public int getStaminaRecoverySpeed() {
        return data.getStaminaRecoverSpeed();
    }

    public int getBaseEnergyRechargeRate() {
        return data.getChargeEfficiency();
    }

    public double getBaseHealth() {
        return data.getHpBase();
    }

    public double getBaseAttack() {
        return data.getAttackBase();
    }

    public double getBaseDefense() {
        return data.getDefenseBase();
    }

    public double getBaseCritRate() {
        return data.getCritical();
    }

    public double getBaseCritDamage() {
        return data.getCriticalHurt();
    }

    public List<CharacterStatGrowth> getStatGrowth() {
        return data.getPropGrowCurves().stream().map(stat -> new CharacterStatGrowth(StatGrowthType.valueOf(stat.getType()), CharacterStatGrowthCurve.valueOf(stat.getGrowCurve()))).collect(Collectors.toList());
    }

    public String getIconName() {
        return data.getIconName();
    }

    public String getSideViewIconName() {
        return data.getSideIconName();
    }

    public String getSplashImageName() {
        return data.getImageName();
    }

    public CharacterUsageType getUsageType() {
        return data.getUseType() != null ? CharacterUsageType.valueOf(data.getUseType()) : CharacterUsageType.NONE;
    }

    public CharacterIdentityType getIdentityType() {
        return CharacterIdentityType.valueOf(data.getAvatarIdentityType());
    }
}
