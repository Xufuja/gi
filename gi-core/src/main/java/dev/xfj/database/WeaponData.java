package dev.xfj.database;

import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.weapon.Weapon;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WeaponData {
    private static List<WeaponExcelConfigDataJson> weaponConfig;

    private WeaponData() {
    }

    public static void init() throws FileNotFoundException {
        weaponConfig = Loader.loadJSONArray(WeaponExcelConfigDataJson.class);
    }

    public static Map<Integer, Weapon> loadWeapons() throws FileNotFoundException {
        if (weaponConfig == null) {
            init();
        }

        return weaponConfig.stream().collect(Collectors.toMap(WeaponExcelConfigDataJson::getId, Weapon::new));
    }
}
