package dev.xfj.weapon;

import dev.xfj.constants.StatGrowthType;
import dev.xfj.constants.WeaponStatGrowthCurve;
import dev.xfj.constants.WeaponType;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class Weapon {
    private final WeaponExcelConfigDataJson data;

    public Weapon(WeaponExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public String getName() {
        return Database.getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getTranslation(data.getDescTextMapHash());
    }

    public WeaponType getWeaponType() {
        return WeaponType.valueOf(data.getWeaponType());
    }

    public int getRarity() {
        return data.getRank();
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
}
