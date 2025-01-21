package dev.xfj.database;

import dev.xfj.jsonschema2pojo.weaponcurveexcelconfigdata.WeaponCurveExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponlevelexcelconfigdata.WeaponLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponpromoteexcelconfigdata.WeaponPromoteExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;

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
}
