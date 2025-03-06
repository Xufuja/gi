package dev.xfj.app.controllers;

import dev.xfj.core.dto.artifact.ArtifactProfileDTO;
import dev.xfj.core.dto.codex.ArtifactSetCodexDTO;
import dev.xfj.core.dto.weapon.WeaponProfileDTO;
import dev.xfj.core.services.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/artifacts")
public class ArtifactController {
    @Autowired
    private ArtifactService artifactService;

    @GetMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ArtifactSetCodexDTO>> artifacts() {
        return ResponseEntity.ok(artifactService.getArtifactSets());
    }

    @GetMapping(
            path = "/{artifactSetId}/{artifactId}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ArtifactProfileDTO> artifact(
            @PathVariable int artifactSetId,
            @PathVariable int artifactId,
            @RequestParam(defaultValue = "1") int level,
            @RequestParam(defaultValue = "0") int experience
    ) {
        if (artifactService.getArtifactSets()
                .stream()
                .filter(entry -> entry.id() == artifactSetId)
                .anyMatch(entry -> Stream.of(
                                entry.flower(),
                                entry.feather(),
                                entry.sands(),
                                entry.goblet(),
                                entry.circlet()
                        )
                        .anyMatch(artifact -> artifact.id() == artifactId))
        ) {
            return ResponseEntity.ok(artifactService.getArtifact(artifactId, level, experience));
        } else {
            return ResponseEntity.badRequest().body(null);
        }

    }
}
