package dev.xfj.app.controllers;

import dev.xfj.core.dto.artifact.ArtifactProfileDTO;
import dev.xfj.core.dto.artifact.ArtifactSetCodexDTO;
import dev.xfj.core.dto.artifact.ArtifactStatsDTO;
import dev.xfj.core.dto.common.StoryDTO;
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

    @GetMapping(
            path = "/{artifactSetId}/{artifactId}/stats",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ArtifactStatsDTO> stats(
            @PathVariable int artifactSetId,
            @PathVariable int artifactId
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
            return ResponseEntity.ok(artifactService.getStats(artifactId));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(
            path = "/{artifactSetId}/stories",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<StoryDTO>> stories(
            @PathVariable int artifactSetId
    ) {
        return ResponseEntity.ok(artifactService.getStories(artifactSetId));
    }

    @GetMapping(
            path = "/{artifactSetId}/{artifactId}/stories",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<StoryDTO>> story(
            @PathVariable int artifactSetId,
            @PathVariable int artifactId
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
            return ResponseEntity.ok(artifactService.getStory(artifactId));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
