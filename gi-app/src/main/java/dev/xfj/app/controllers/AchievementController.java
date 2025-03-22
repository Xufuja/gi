package dev.xfj.app.controllers;

import dev.xfj.core.dto.achievement.AchievementCodexDTO;
import dev.xfj.core.services.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/achievements")
public class AchievementController {
    @Autowired
    private AchievementService achievementService;

    @GetMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<AchievementCodexDTO>> achievements() {
        return ResponseEntity.ok(achievementService.getAchievements());
    }
}
