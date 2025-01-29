package dev.xfj.app.controllers;

import dev.xfj.core.dto.CharacterDTO;
import dev.xfj.core.services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class CharacterController {
    @Autowired
    private CharacterService characterService;

    @GetMapping(path = "/v1/characters", consumes = APPLICATION_JSON_VALUE)
    public CharacterDTO test() {
        return characterService.test();
    }
}
