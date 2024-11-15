package dev.xfj.weapon;

import dev.xfj.constants.*;
import dev.xfj.item.Item;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public record Weapon(WeaponExcelConfigDataJson data) implements Item<WeaponExcelConfigDataJson> {

    public WeaponType getWeaponType() {
        return WeaponType.valueOf(data.getWeaponType());
    }

    public int getBaseRequiredExp() {
        return data.getWeaponBaseExp();
    }

    public List<Integer> getSkills() {
        return data.getSkillAffix();
    }

    public List<WeaponStatGrowth> getStatGrowth() {
        return data.getWeaponProp().stream().map(stat -> new WeaponStatGrowth(stat.getPropType() != null ? StatType.valueOf(stat.getPropType()) : StatType.NONE, stat.getInitValue(), WeaponStatGrowthCurve.valueOf(stat.getType()))).collect(Collectors.toList());
    }

    public List<Integer> getRefinementCost() {
        return data.getAwakenCosts();
    }

    public boolean isInitiallyLocked() {
        return data.getInitialLockState() == 2;
    }

    public String getRefinedIconName() {
        return data.getAwakenIcon();
    }

    @Override
    public String toString() {
        return getName();
    }
}
