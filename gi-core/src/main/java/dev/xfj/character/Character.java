package dev.xfj.character;

import dev.xfj.constants.*;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class Character {
    private final AvatarExcelConfigDataJson avatar;

    public Character(AvatarExcelConfigDataJson avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return avatar.getId();
    }

    public String getName() {
        return Database.getTranslation(avatar.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getTranslation(avatar.getDescTextMapHash());
    }

    public int getRarity() {
        return CharacterRarity.valueOf(avatar.getQualityType()).getStarValue();
    }

    public CharacterBodyType getBodyType() {
        return CharacterBodyType.valueOf(avatar.getBodyType());
    }

    public WeaponType getWeaponType() {
        return WeaponType.valueOf(avatar.getWeaponType());
    }

    public int getStartingWeaponId() {
        return avatar.getInitialWeapon();
    }

    public int getDefaultSkillTreeId() {
        return avatar.getSkillDepotId();
    }

    public List<Integer> getAlternateSkillTreeIds() {
        return avatar.getCandSkillDepotIds();
    }

    public int getAscensionId() {
        return avatar.getAvatarPromoteId();
    }

    public List<Integer> getAscensionRewardLevels() {
        return avatar.getAvatarPromoteRewardLevelList();
    }

    public List<Integer> getAscensionRewardIds() {
        return avatar.getAvatarPromoteRewardIdList();
    }

    public int getStaminaRecoverySpeed() {
        return avatar.getStaminaRecoverSpeed();
    }

    public int getBaseEnergyRechargeMultiplier() {
        return avatar.getChargeEfficiency();
    }

    public double getBaseHealth() {
        return avatar.getHpBase();
    }

    public double getBaseAttack() {
        return avatar.getAttackBase();
    }

    public double getBaseDefense() {
        return avatar.getDefenseBase();
    }

    public double getBaseCritRate() {
        return avatar.getCritical();
    }

    public double getBaseCritDamage() {
        return avatar.getCriticalHurt();
    }

    public List<CharacterStatGrowth> getStatGrowth() {
        return avatar.getPropGrowCurves().stream().map(stat -> new CharacterStatGrowth(CharacterStatGrowthType.valueOf(stat.getType()), CharacterStatGrowthCurve.valueOf(stat.getGrowCurve()))).collect(Collectors.toList());
    }

    public String getIconName() {
        return avatar.getIconName();
    }

    public String getSideViewIconName() {
        return avatar.getSideIconName();
    }

    public String getSplashImageName() {
        return avatar.getImageName();
    }

    public CharacterUsageType getUsageType() {
        return avatar.getUseType() != null ? CharacterUsageType.valueOf(avatar.getUseType()) : CharacterUsageType.NONE;
    }

    public CharacterIdentityType getIdentityType() {
        return CharacterIdentityType.valueOf(avatar.getAvatarIdentityType());
    }
}
