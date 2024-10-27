package dev.xfj.character;

import dev.xfj.constants.*;
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

    public CharacterUsageType getUsageType() {
        return avatar.getUseType() != null ? CharacterUsageType.valueOf(avatar.getUseType()) : CharacterUsageType.NONE;
    }

    public long getNameHash() {
        return avatar.getNameTextMapHash();
    }

    public CharacterBodyType getBodyType() {
        return CharacterBodyType.valueOf(avatar.getBodyType());
    }

    public int getRarity() {
        return CharacterRarity.valueOf(avatar.getQualityType()).getStar();
    }

    public WeaponType getWeaponType() {
        return WeaponType.valueOf(avatar.getWeaponType());
    }

    public CharacterIdentityType getIdentityType() {
        return CharacterIdentityType.valueOf(avatar.getAvatarIdentityType());
    }

    public List<CharacterStatGrowth> getStatGrowth() {
        return avatar.getPropGrowCurves().stream().map(stat -> new CharacterStatGrowth(CharacterStatGrowthType.valueOf(stat.getType()), CharacterStatGrowthCurve.valueOf(stat.getGrowCurve()))).collect(Collectors.toList());
    }
}
