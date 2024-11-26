package dev.xfj.weapon;

import dev.xfj.jsonschema2pojo.weaponlevelexcelconfigdata.WeaponLevelExcelConfigDataJson;

public class WeaponLevel {
    private final WeaponLevelExcelConfigDataJson data;
    private final int rank;

    public WeaponLevel(WeaponLevelExcelConfigDataJson data, int rank) {
        this.data = data;
        this.rank = rank;
    }

    public int getRank() {
        return rank + 1;
    }

    public int getLevel() {
        return data.getLevel();
    }

    public int getRequiredExp() {
        return data.getRequiredExps().get(rank);
    }
}
