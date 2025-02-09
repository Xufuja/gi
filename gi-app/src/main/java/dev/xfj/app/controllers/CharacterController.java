package dev.xfj.app.controllers;

import dev.xfj.core.dto.character.CharacterProfileDTO;
import dev.xfj.core.dto.character.TalentsDTO;
import dev.xfj.core.dto.codex.CharacterCodexDTO;
import dev.xfj.core.services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class CharacterController {
    private static final String BASE_PATH = "/v1/characters";

    @Autowired
    private CharacterService characterService;

    @GetMapping(
            path = BASE_PATH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CharacterCodexDTO>> characters() {
        return ResponseEntity.ok(characterService.getCharacters());
    }

    @GetMapping(
            path = BASE_PATH + "/{characterId}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CharacterProfileDTO> character(
            @PathVariable int characterId,
            @RequestParam(defaultValue = "1") int level,
            @RequestParam(defaultValue = "0") int experience,
            @RequestParam(defaultValue = "0") int ascension
    ) {
        return ResponseEntity.ok(characterService.getCharacter(characterId, level, experience, ascension));
    }

    @GetMapping(
            path = BASE_PATH + "/{characterId}/talents",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TalentsDTO> talents(
            @PathVariable int characterId,
            @RequestParam(defaultValue = "1") int level
    ) {
        return ResponseEntity.ok(characterService.getTalents(characterId, level));
    }
}
