package dev.xfj.app.controllers;

import dev.xfj.core.dto.codex.ArtifactSetCodexDTO;
import dev.xfj.core.services.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
