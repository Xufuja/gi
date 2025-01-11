package dev.xfj.codex;

import dev.xfj.database.*;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
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

    public static List<ArtifactSetCodex> getArtifactSets() {
        return ReliquaryData.getInstance().reliquaryCodexConfig
                .stream()
                .sorted(Comparator.comparing(ReliquaryCodexExcelConfigDataJson::getSortOrder))
                .map(ArtifactSetCodex::new)
                .collect(Collectors.toList());
    }

    public static List<ItemCodex> getItems() {
        return ItemData.getInstance().materialCodexConfig
                .stream()
                .sorted(Comparator.comparing(MaterialCodexExcelConfigDataJson::getSortOrder))
                .map(ItemCodex::new)
                .collect(Collectors.toList());
    }
}
