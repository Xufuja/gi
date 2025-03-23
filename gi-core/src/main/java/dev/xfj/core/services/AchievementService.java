package dev.xfj.core.services;

import dev.xfj.core.dto.achievement.AchievementCodexDTO;
import dev.xfj.core.dto.achievement.AchievementDTO;
import dev.xfj.generated.achievementexcelconfigdata.AchievementExcelConfigDataJson;
import dev.xfj.generated.achievementgoalexcelconfigdata.AchievementGoalExcelConfigDataJson;
import dev.xfj.generated.materialexcelconfigdata.MaterialExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementService {
    private final DatabaseService databaseService;

    @Autowired
    public AchievementService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<AchievementCodexDTO> getAchievements() {
        return databaseService.achievementGoalConfig
                .stream()
                .sorted(Comparator.comparing(AchievementGoalExcelConfigDataJson::getOrderId))
                .map(entry -> new AchievementCodexDTO(
                        entry.getId(),
                        databaseService.getTranslation(entry.getNameTextMapHash()),
                        getAchievementsByCategory(entry.getId()),
                        entry.getOrderId()
                ))
                .collect(Collectors.toList());
    }

    private List<AchievementDTO> getAchievementsByCategory(int category) {
        return databaseService.achievementConfig
                .stream()
                .filter(achievement -> achievement.getGoalId() == category)
                .filter(entry -> entry.getOrderId() != 0)
                .sorted(Comparator.comparing(AchievementExcelConfigDataJson::getOrderId))
                .map(achievement -> new AchievementDTO(
                        achievement.getId(),
                        databaseService.getTranslation(achievement.getTitleTextMapHash()),
                        databaseService.getTranslation(achievement.getDescTextMapHash()),
                        achievement.getOrderId()
                ))
                .collect(Collectors.toList());
    }
}
