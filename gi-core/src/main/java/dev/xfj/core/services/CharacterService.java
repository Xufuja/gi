package dev.xfj.core.services;

import dev.xfj.core.codex.CharacterCodex;
import dev.xfj.core.dto.CharacterCodexDTO;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterService {
    public List<CharacterCodexDTO> getCharacters() {
        return DatabaseService.getInstance().avatarCodexConfig
                .stream()
                .sorted(Comparator.comparing(AvatarCodexExcelConfigDataJson::getSortFactor))
                .map(CharacterCodex::new)
                .toList()
                .stream()
                .map(entry -> new CharacterCodexDTO(
                        entry.getId(),
                        entry.getName(),
                        entry.getReleaseTime(),
                        entry.getSortFactor())
                )
                .collect(Collectors.toList());
    }
}
