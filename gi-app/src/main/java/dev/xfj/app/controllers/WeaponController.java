package dev.xfj.app.controllers;

import dev.xfj.core.dto.codex.WeaponCodexDTO;
import dev.xfj.core.services.WeaponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
