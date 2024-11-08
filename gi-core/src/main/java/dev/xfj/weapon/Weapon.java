package dev.xfj.weapon;

import dev.xfj.constants.*;
import dev.xfj.database.Database;
import dev.xfj.item.Item;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class Weapon implements Item {
    private final WeaponExcelConfigDataJson data;

    public Weapon(WeaponExcelConfigDataJson data) {
        this.data = data;
    }

    @Override
    public int getId() {
        return data.getId();
    }

    @Override
    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    @Override
    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    public int getRarity() {
        return data.getRank();
    }

    public WeaponType getWeaponType() {
        return WeaponType.valueOf(data.getWeaponType());
    }

    @Override
    public ItemType getItemType() {
        return ItemType.valueOf(data.getItemType());
    }

    public int getBaseRequiredExp() {
        return data.getWeaponBaseExp();
    }

    public List<Integer> getSkills() {
        return data.getSkillAffix();
    }

    public List<WeaponStatGrowth> getStatGrowth() {
        return data.getWeaponProp().stream().map(stat -> new WeaponStatGrowth(stat.getPropType() != null ? StatGrowthType.valueOf(stat.getPropType()) : StatGrowthType.NONE, stat.getInitValue(), WeaponStatGrowthCurve.valueOf(stat.getType()))).collect(Collectors.toList());
    }

    public List<Integer> getRefinementCost() {
        return data.getAwakenCosts();
    }

    public boolean isInitiallyLocked() {
        return data.getInitialLockState() == 2;
    }

    @Override
    public SalvageType getSalvageType() {
        return SalvageType.valueOf(data.getDestroyRule());
    }

    @Override
    public SalvageReturnItems getSalvagedItems() {
        //There is never more than 1 item in either array
        return new SalvageReturnItems(
                data.getDestroyReturnMaterial().get(0),
                data.getDestroyReturnMaterialCount().get(0)
        );
    }

    @Override
    public String getIconName() {
        return data.getIcon();
    }

    public String getRefinedIconName() {
        return data.getAwakenIcon();
    }

    @Override
    public Integer getWeight() {
        return data.getWeight();
    }

    @Override
    public Integer getRank() {
        return data.getRank();
    }

    @Override
    public Integer getGadgetId() {
        return data.getGadgetId();
    }

    @Override
    public String toString() {
        return getName();
    }
}
