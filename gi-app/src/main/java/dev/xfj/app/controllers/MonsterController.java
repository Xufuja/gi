package dev.xfj.app.controllers;

import dev.xfj.core.dto.artifact.ArtifactStatsDTO;
import dev.xfj.core.dto.monster.MonsterCodexDTO;
import dev.xfj.core.dto.monster.MonsterProfileDTO;
import dev.xfj.core.dto.monster.MonsterStatsDTO;
import dev.xfj.core.services.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/monsters")
public class MonsterController {
    @Autowired
    private MonsterService monsterService;

    @GetMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<MonsterCodexDTO>> items() {
        return ResponseEntity.ok(monsterService.getMonsters());
    }

    @GetMapping(
            path = "/{monsterId}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MonsterProfileDTO> item(@PathVariable int monsterId) {

        return ResponseEntity.ok(monsterService.getMonster(monsterId));
    }

    @GetMapping(
            path = "/{monsterId}/stats",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<MonsterStatsDTO>> stats(
            @PathVariable int monsterId
    ) {
        return ResponseEntity.ok(monsterService.getStats(monsterId));

    }
}
