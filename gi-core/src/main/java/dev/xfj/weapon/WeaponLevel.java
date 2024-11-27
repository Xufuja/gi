package dev.xfj.weapon;

import dev.xfj.artifact.Level;
import dev.xfj.jsonschema2pojo.weaponlevelexcelconfigdata.WeaponLevelExcelConfigDataJson;

public class WeaponLevel implements Level {
    private final WeaponLevelExcelConfigDataJson data;
    private final int rank;

    public WeaponLevel(WeaponLevelExcelConfigDataJson data, int rank) {
        this.data = data;
        this.rank = rank;
    }

    public int getRank() {
        return rank + 1;
    }

    @Override
    public int getLevel() {
        return data.getLevel();
    }

    @Override
    public int getRequiredExp() {
        return data.getRequiredExps().get(rank);
    }
}
