package dev.xfj.app.controllers;

import dev.xfj.core.dto.common.MaterialsDTO;
import dev.xfj.core.dto.common.StoryDTO;
import dev.xfj.core.dto.codex.WeaponCodexDTO;
import dev.xfj.core.dto.weapon.WeaponProfileDTO;
import dev.xfj.core.services.WeaponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/weapons")
public class WeaponController {
    @Autowired
    private WeaponService weaponService;

    @GetMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<WeaponCodexDTO>> weapons() {
        return ResponseEntity.ok(weaponService.getWeapons());
    }

    @GetMapping(
            path = "/{weaponId}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WeaponProfileDTO> weapon(
            @PathVariable int weaponId,
            @RequestParam(defaultValue = "1") int level,
            @RequestParam(defaultValue = "0") int experience,
            @RequestParam(defaultValue = "0") int ascension,
            @RequestParam(defaultValue = "1") int refinement
    ) {
        return ResponseEntity.ok(weaponService.getWeapon(weaponId, level, experience, ascension, refinement));
    }

    @GetMapping(
            path = "/{weaponId}/materials",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MaterialsDTO> materials(
            @PathVariable int weaponId
    ) {
        return ResponseEntity.ok(weaponService.getMaterials(weaponId));
    }

    @GetMapping(
            path = "/{weaponId}/stories",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<StoryDTO>> stories(
            @PathVariable int weaponId
    ) {
        return ResponseEntity.ok(weaponService.getStories(weaponId));
    }
}
