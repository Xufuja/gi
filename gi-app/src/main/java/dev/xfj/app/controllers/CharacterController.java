package dev.xfj.app.controllers;

import dev.xfj.core.dto.character.CharacterProfileDTO;
import dev.xfj.core.dto.character.ConstellationDTO;
import dev.xfj.core.dto.character.MaterialsDTO;
import dev.xfj.core.dto.character.TalentsDTO;
import dev.xfj.core.dto.codex.CharacterCodexDTO;
import dev.xfj.core.services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/characters")
public class CharacterController {
    @Autowired
    private CharacterService characterService;

    @GetMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CharacterCodexDTO>> characters() {
        return ResponseEntity.ok(characterService.getCharacters());
    }

    @GetMapping(
            path = "/{characterId}",
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
            path = "/{characterId}/talents",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TalentsDTO> talents(
            @PathVariable int characterId,
            @RequestParam(defaultValue = "1") int level
    ) {
        return ResponseEntity.ok(characterService.getTalents(characterId, level));
    }

    @GetMapping(
            path = "/{characterId}/constellations",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ConstellationDTO>> constellations(
            @PathVariable int characterId
    ) {
        return ResponseEntity.ok(characterService.getConstellations(characterId));
    }

    @GetMapping(
            path = "/{characterId}/materials",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MaterialsDTO> materials(
            @PathVariable int characterId
    ) {
        return ResponseEntity.ok(characterService.getMaterials(characterId));
    }
}
