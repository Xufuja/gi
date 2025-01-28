package dev.xfj.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CharacterController {
    @GetMapping("/")
    public String test() {
        return "Test";
    }
}
