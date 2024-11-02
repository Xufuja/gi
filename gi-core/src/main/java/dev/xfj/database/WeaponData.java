package dev.xfj.database;

import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.weapon.Weapon;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class WeaponData implements Data {
    private static WeaponData instance;
    private final List<WeaponExcelConfigDataJson> weaponConfig;

    private WeaponData() throws FileNotFoundException {
        weaponConfig = loadJSONArray(WeaponExcelConfigDataJson.class);
    }

    public static WeaponData getInstance() {
        if (instance == null) {
            try {
                instance = new WeaponData();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return instance;
    }

    public Map<Integer, Weapon> loadWeapons() {
        return loadDataWithId(Weapon.class, weaponConfig);
    }
}
