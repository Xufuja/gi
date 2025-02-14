package dev.xfj.core.services;

import dev.xfj.core.dto.codex.ArtifactEntryDTO;
import dev.xfj.core.dto.codex.ArtifactSetCodexDTO;
import dev.xfj.jsonschema2pojo.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArtifactService {
    private final DatabaseService databaseService;

    @Autowired
    public ArtifactService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<ArtifactSetCodexDTO> getArtifactSets() {
        return databaseService.reliquaryCodexConfig
                .stream()
                .sorted(Comparator.comparing(ReliquaryCodexExcelConfigDataJson::getSortOrder))
                .map(data -> new ArtifactSetCodexDTO(
                        data.getSuitId(),
                        data.getLevel(),
                        filter(Map.of(data.getCupId(), getName(getArtifact(data.getCupId())))),
                        filter(Map.of(data.getLeatherId(), getName(getArtifact(data.getLeatherId())))),
                        filter(Map.of(data.getCapId(), getName(getArtifact(data.getCapId())))),
                        filter(Map.of(data.getFlowerId(), getName(getArtifact(data.getFlowerId())))),
                        filter(Map.of(data.getSandId(), getName(getArtifact(data.getSandId())))),
                        data.getSortOrder())
                )
                .collect(Collectors.toList());
    }

    private ReliquaryExcelConfigDataJson getArtifact(int id) {
        return databaseService.reliquaryConfig
                .stream()
                .filter(artifact -> artifact.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private String getName(ReliquaryExcelConfigDataJson artifact) {
        if (artifact != null) {
            return databaseService.getTranslation(artifact.getNameTextMapHash());
        } else {
            return "";
        }
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
