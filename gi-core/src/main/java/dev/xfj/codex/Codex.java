package dev.xfj.codex;

import dev.xfj.database.AvatarData;
import dev.xfj.database.Database;
import dev.xfj.database.WeaponData;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Codex {
    public static List<CharacterCodex> getCharacters() {
        return AvatarData.getInstance().avatarCodexConfig
                .stream()
                .sorted(Comparator.comparing(AvatarCodexExcelConfigDataJson::getSortFactor))
                .map(CharacterCodex::new)
                .collect(Collectors.toList());
    }

    public static List<WeaponCodex> getWeapons() {
        return WeaponData.getInstance().weaponConfig
                .stream()
                .sorted(Comparator.comparing(WeaponExcelConfigDataJson::getId))
                .map(entry -> new WeaponCodex(entry.getId(), Database.getInstance().getTranslation(entry.getNameTextMapHash())))
                .collect(Collectors.toList());
    }
}
