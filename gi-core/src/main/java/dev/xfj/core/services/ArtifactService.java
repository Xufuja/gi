package dev.xfj.core.services;

import dev.xfj.core.codex.ArtifactSetCodex;
import dev.xfj.core.dto.ArtifactEntryDTO;
import dev.xfj.core.dto.ArtifactSetCodexDTO;
import dev.xfj.jsonschema2pojo.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArtifactService {
    public List<ArtifactSetCodexDTO> getArtifactSets() {
        return DatabaseService.getInstance().reliquaryCodexConfig
                .stream()
                .sorted(Comparator.comparing(ReliquaryCodexExcelConfigDataJson::getSortOrder))
                .map(ArtifactSetCodex::new)
                .toList()
                .stream()
                .map(entry -> new ArtifactSetCodexDTO(
                        entry.getId(),
                        entry.getRarity(),
                        filter(entry.getGoblet()),
                        filter(entry.getFeather()),
                        filter(entry.getCirclet()),
                        filter(entry.getFlower()),
                        filter(entry.getSands()),
                        entry.getSortFactor())
                )
                .collect(Collectors.toList());
    }

    private ArtifactEntryDTO filter(Map<Integer, String> artifact) {
        if (artifact.containsKey(0)) {
            return null;
        }

        return artifact.entrySet()
                .stream()
                .map(entry -> new ArtifactEntryDTO(entry.getKey(), entry.getValue()))
                .findFirst()
                .orElse(null);
    }
}
