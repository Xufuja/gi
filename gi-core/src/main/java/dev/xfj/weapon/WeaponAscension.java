package dev.xfj.weapon;

import dev.xfj.character.Ascension;
import dev.xfj.jsonschema2pojo.weaponpromoteexcelconfigdata.WeaponPromoteExcelConfigDataJson;

public record WeaponAscension(
        WeaponPromoteExcelConfigDataJson data) implements Ascension<WeaponPromoteExcelConfigDataJson> {

    @Override
    public int getAscensionId() {
        return data.getWeaponPromoteId();
    }

    @Override
    public int getAscensionCost() {
        return data.getCoinCost();
    }
}
