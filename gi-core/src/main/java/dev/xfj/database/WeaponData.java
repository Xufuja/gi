package dev.xfj.database;

import dev.xfj.jsonschema2pojo.weaponcurveexcelconfigdata.WeaponCurveExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponlevelexcelconfigdata.WeaponLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponpromoteexcelconfigdata.WeaponPromoteExcelConfigDataJson;
import dev.xfj.weapon.Weapon;
import dev.xfj.weapon.WeaponAscension;
import dev.xfj.weapon.WeaponLevel;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeaponData implements Data {
    private static WeaponData instance;
    public final List<WeaponExcelConfigDataJson> weaponConfig;
    public final List<WeaponPromoteExcelConfigDataJson> weaponPromoteConfig;
    public final List<WeaponLevelExcelConfigDataJson> weaponLevelConfig;
    public final List<WeaponCurveExcelConfigDataJson> weaponCurveConfig;

    private WeaponData() throws FileNotFoundException {
        weaponConfig = loadJSONArray(WeaponExcelConfigDataJson.class);
        weaponPromoteConfig = loadJSONArray(WeaponPromoteExcelConfigDataJson.class);
        weaponLevelConfig = loadJSONArray(WeaponLevelExcelConfigDataJson.class);
        weaponCurveConfig = loadJSONArray(WeaponCurveExcelConfigDataJson.class);
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
        return loadDataWithIntegerId(Weapon.class, weaponConfig);
    }

    public Map<Integer, Map<Integer, WeaponAscension>> loadWeaponAscensions() {
        return loadNestedDataWithIds(
                WeaponAscension.class,
                weaponPromoteConfig,
                "getWeaponPromoteId",
                "getAscension");
    }

    public Map<Integer, Map<Integer, WeaponLevel>> loadLevelRequirements() {
        Map<Integer, Map<Integer, WeaponLevel>> result = new HashMap<>();

        IntStream.range(0, 5).forEach(i -> {
            Map<Integer, WeaponLevel> perLevel = weaponLevelConfig
                    .stream()
                    .collect(Collectors.toMap(
                            WeaponLevelExcelConfigDataJson::getLevel,
                            level -> new WeaponLevel(level, i))
                    );
            result.put(i + 1, perLevel);
        });

        return result;
    }
}
