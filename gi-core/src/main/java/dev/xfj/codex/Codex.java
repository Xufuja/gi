package dev.xfj.codex;

import dev.xfj.database.AvatarData;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;

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
}
